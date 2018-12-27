package app.fmgp.money.instances

import app.fmgp.money.{MoneyTree, MoneyZBranch, MoneyZLeaf}
import cats.{Functor, Monad}


trait MoneyTreeInstances {
  implicit val MoneyTreeFunctor: Functor[MoneyTree] = new MoneyTreeFunctor
  implicit val MoneyTreeMonad: Monad[MoneyTree] = new MoneyTreeMonad
}

class MoneyTreeFunctor extends Functor[MoneyTree] {
  def map[A, B](tree: MoneyTree[A])(func: A => B): MoneyTree[B] =
    tree match {
      case MoneyZBranch(value) => MoneyZBranch(value.map(e => map(e)(func)))
      case MoneyZLeaf(value) => MoneyZLeaf[B](func(value))
    }
}

class MoneyTreeMonad extends Monad[MoneyTree] {
  override def pure[A](x: A): MoneyTree[A] = MoneyZLeaf(x)

  override def flatMap[A, B](fa: MoneyTree[A])(f: A => MoneyTree[B]): MoneyTree[B] =
    fa match {
      case MoneyZBranch(v: Seq[MoneyTree[A]]) => MoneyZBranch[B](v.map { e => flatMap(e)(f) })
      case MoneyZLeaf(v) => f(v)
    }

  //TODO MoneyTree.monad.tailRecM stack safety *** FAILED *** */
  override def tailRecM[A, B](x: A)(f: A => MoneyTree[Either[A, B]]): MoneyTree[B] =
    flatMap(f(x)) {
      case Left(value) => tailRecM(value)(f)
      case Right(value) => MoneyZLeaf(value)
    }

  //override def tailRecM[A, B](x: A)(f: A => MoneyTree[Either[A, B]]): MoneyTree[B] = {
  //  @scala.annotation.tailrec
  //  def loop(
  //    open: List[MoneyTree[Either[A, B]]],
  //    closed: List[Option[MoneyTree[B]]] //, structure
  //  ): List[MoneyTree[B]] = open match {
  //    case MoneyZBranch(seq) :: next => loop(seq.toList ++ next, closed) //TODO need to store the structure
  //    case MoneyZLeaf(Left(a)) :: next => loop(f(a) :: next, closed)
  //    case MoneyZLeaf(Right(b)) :: next => loop(next, Some(pure(b)) :: closed)
  //    case Nil =>
  //      closed.foldLeft(Nil: List[MoneyTree[B]]) { (acc, maybeTree) =>
  //        maybeTree.map(_ :: acc).getOrElse {
  //          val fffff :: tail = acc
  //          MoneyZBranch(Seq(fffff)) :: tail //FAIL !!!! and now???
  //        }
  //      }
  //  }
  //  loop(List(f(x)), Nil).head
  //}
}

