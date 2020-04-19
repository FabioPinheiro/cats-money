package app.fmgp.money.instances

import app.fmgp.money.{MoneyTree, MoneyZBranch, MoneyZLeaf}
import cats.{Applicative, Eval, Functor, Monad, Show, Traverse}

trait MoneyTreeInstances {
  implicit def MoneyTreeShow[T](implicit showT: Show[T]): Show[MoneyTree[T]] =
    Show.show(elem => {
      def loop(mt: MoneyTree[T])(implicit showT: Show[T]): String = mt match {
        case MoneyZBranch(value) => value.map(e => loop(e)).mkString("[", " + ", "]")
        case MoneyZLeaf(value)   => showT.show(value)
      }

      loop(elem)
    })
  implicit val MoneyTreeFunctor: Functor[MoneyTree] = new MoneyTreeFunctor
  implicit val MoneyTreeMonad: Monad[MoneyTree] = new MoneyTreeMonad
  val MoneyTreeTraverse: Traverse[MoneyTree] = new MoneyTreeTraverse
}

class MoneyTreeFunctor extends Functor[MoneyTree] {
  def map[A, B](tree: MoneyTree[A])(func: A => B): MoneyTree[B] =
    tree match {
      case MoneyZBranch(value) => MoneyZBranch(value.map(e => map(e)(func)))
      case MoneyZLeaf(value)   => MoneyZLeaf[B](func(value))
    }
}

class MoneyTreeApplicative extends Applicative[MoneyTree] {
  override def pure[A](x: A): MoneyTree[A] = MoneyZLeaf(x)
  override def ap[A, B](ff: MoneyTree[A => B])(fa: MoneyTree[A]): MoneyTree[B] = fa match {
    case MoneyZBranch(seq) => MoneyZBranch(seq.map(ap(ff)))
    case MoneyZLeaf(v) =>
      ff match {
        case MoneyZLeaf(a2b) => MoneyZLeaf(a2b(v))
        case x @ MoneyZBranch(_) =>
          x.collectValues.headOption
            .map(a2b => MoneyTree.one(a2b(v)))
            .getOrElse(MoneyTree.empty) //HUM ... (this inner match is bad)
      }
  }
}

class MoneyTreeTraverse extends Traverse[MoneyTree] {
  override def traverse[G[_], A, B](fa: MoneyTree[A])(f: A => G[B])(
      implicit evidence: Applicative[G]
  ): G[MoneyTree[B]] = ???
  override def foldLeft[A, B](fa: MoneyTree[A], b: B)(f: (B, A) => B): B = fa.collectValues.foldLeft(b)(f)
  override def foldRight[A, B](fa: MoneyTree[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = ???
}

class MoneyTreeMonad extends Monad[MoneyTree] {
  override def pure[A](x: A): MoneyTree[A] = MoneyZLeaf(x)

  override def flatMap[A, B](fa: MoneyTree[A])(f: A => MoneyTree[B]): MoneyTree[B] =
    fa match {
      case MoneyZBranch(v: Seq[MoneyTree[A]]) => MoneyZBranch[B](v.map { e => flatMap(e)(f) })
      case MoneyZLeaf(v)                      => f(v)
    }

  /**
    * Stack Safety for Free implementation
    * A
    * B    C    D
    * E   F       G
    * H I
    *
    * ((E(HI))C(G)) <- all leafs
    *
    * A       .        | .
    * BCD     33       | .
    * EFCD    22 33    | .
    * FCD     12 33    | E
    * HICD    22 12 33 | E
    * ICD     12 12 33 | HE
    * CD      -2 12 33 | IHE
    * CD      -2 33    | (HI)E
    * CD      23       | (E(HI))
    * D       13       | C(E(HI))
    * G       11 13    | C(E(HI))
    * .       -1 13    | GC(E(HI))
    * .       -3       | (G)C(E(HI))
    * .       .        | ((E(HI))C(G))
    *
    */
  override def tailRecM[A, B](x: A)(f: A => MoneyTree[Either[A, B]]): MoneyTree[B] = {
    //    flatMap(f(x)) {
    //      case Left(value) => tailRecM(value)(f)
    //      case Right(value) => MoneyZLeaf(value)
    //    }
    @scala.annotation.tailrec
    def loop(
        open: List[MoneyTree[Either[A, B]]],
        closed: List[MoneyTree[B]],
        state: List[(Int, Int)]
    ): MoneyTree[B] = {
      (open, state) match {
        case (Nil, Nil) if closed.size == 1     => closed.head
        case (MoneyZLeaf(Right(b)) :: Nil, Nil) => MoneyTree.one(b)

        case (Nil, (0, _) :: Nil) => MoneyTree.branch(closed.reverse)
        case (Nil, _)             => throw new RuntimeException() //TODO is this an impossible case or not?
        case (_, (0, t) :: (_s, _t) :: tail) =>
          assert(closed.size >= t)
          loop(open, MoneyZBranch(closed.take(t).reverse) :: closed.drop(t), (_s - 1, _t) :: tail)
        case (MoneyZLeaf(Left(a)) :: next, _) =>
          loop(f(a) :: next, closed, state)
        case (MoneyZLeaf(Right(b)) :: next, (s, t) :: stateTail) =>
          loop(next, MoneyTree.one(b) :: closed, (s - 1, t) :: stateTail)
        case (MoneyZBranch(Nil) :: next, (_s, _t) :: stateTail) =>
          loop(next, MoneyTree.empty :: closed, (_s - 1, _t) :: stateTail)
        case (MoneyZBranch(seq) :: next, _state) =>
          loop(seq.toList ++ next, closed, (seq.size, seq.size) :: _state)
      }
    }

    loop(List(f(x)), Nil, List.empty)
  }
}
