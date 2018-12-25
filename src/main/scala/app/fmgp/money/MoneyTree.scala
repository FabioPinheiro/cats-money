package app.fmgp.money

import app.fmgp.money.CurrencyY.CurrencyY
import cats.{Functor, Monad}
import cats.syntax.monoid._
import cats.instances.bigDecimal._
import cats.instances.map._
import cats.kernel.{CommutativeGroup, Eq, Monoid, Order}

sealed abstract class MoneyTree[+A] {
  def collapse: MoneyTree[A]
  def collectValues: Seq[A]
  def simplify[B >: A](implicit monoid: Monoid[B]): MoneyTree[B] = {
    val as: TraversableOnce[A] = this.collectValues
    val a: B = monoid.combineAll(as)
    MoneyTree.joinLeaf(a)
  }
}
final case class MoneyZBranch[A](value: Seq[MoneyTree[A]]) extends MoneyTree[A] {
  override def collectValues: Seq[A] = value.map(_.collapse).flatMap {
    case MoneyZBranch(v: Seq[MoneyTree[A]]) => v.flatMap(_.collectValues)
    case o@MoneyZLeaf(_) => o.collectValues //Seq(v)
  }
  override def collapse: MoneyZBranch[A] = MoneyZBranch[A](value.flatMap(_.collectValues).map(MoneyZLeaf.apply))
}
final case class MoneyZLeaf[A](value: A) extends MoneyTree[A] {
  override def collectValues: Seq[A] = Seq(value)
  override def collapse: MoneyZLeaf[A] = this
}


object MoneyTree {
  def empty[M] = MoneyZBranch[M](Seq.empty)
  def branch[M](m: Seq[MoneyTree[M]]): MoneyTree[M] = MoneyZBranch[M](m)
  def join[M](m: MoneyTree[M]*): MoneyTree[M] = MoneyZBranch[M](m)
  def joinLeaf[M](m: M*): MoneyTree[M] = MoneyZBranch[M](m.map(leaf))
  def leaf[M](value: M): MoneyTree[M] = MoneyZLeaf[M](value)

  implicit def eqTree[A: Eq]: Eq[MoneyTree[A]] = Eq.fromUniversalEquals
  implicit val treeFunctor: Functor[MoneyTree] = new Functor[MoneyTree] {
    def map[A, B](tree: MoneyTree[A])(func: A => B): MoneyTree[B] =
      tree match {
        case MoneyZBranch(value) => MoneyZBranch(value.map(e => map(e)(func)))
        case MoneyZLeaf(value) => MoneyZLeaf[B](func(value))
      }
  }
  implicit val treeMonad: Monad[MoneyTree] = new Monad[MoneyTree] {
    override def flatMap[A, B](fa: MoneyTree[A])(f: A => MoneyTree[B]): MoneyTree[B] = {
      fa match {
        case MoneyZBranch(v: Seq[MoneyTree[A]]) => MoneyZBranch[B](v.map { e => flatMap(e)(f) })
        case MoneyZLeaf(v) => f(v)
      }
    }
    //TODO MoneyTree.monad.tailRecM stack safety *** FAILED *** */
    override def tailRecM[A, B](x: A)(f: A => MoneyTree[Either[A, B]]): MoneyTree[B] =
      flatMap(f(x)) {
        case Left(value) => tailRecM(value)(f)
        case Right(value) => MoneyZLeaf(value)
      }

    //    override def tailRecM[A, B](x: A)(f: A => MoneyTree[Either[A, B]]): MoneyTree[B] = {
    //      @scala.annotation.tailrec
    //      def loop(
    //        open: List[MoneyTree[Either[A, B]]],
    //        closed: List[Option[MoneyTree[B]]] //, structure
    //      ): List[MoneyTree[B]] = open match {
    //        case MoneyZBranch(seq) :: next => loop(seq.toList ++ next, closed) //TODO need to store the structure
    //        case MoneyZLeaf(Left(a)) :: next => loop(f(a) :: next, closed)
    //        case MoneyZLeaf(Right(b)) :: next => loop(next, Some(pure(b)) :: closed)
    //        case Nil =>
    //          closed.foldLeft(Nil: List[MoneyTree[B]]) { (acc, maybeTree) =>
    //            maybeTree.map(_ :: acc).getOrElse {
    //              val fffff :: tail = acc
    //              MoneyZBranch(Seq(fffff)) :: tail //FAIL !!!! and now???
    //            }
    //          }
    //      }
    //      loop(List(f(x)), Nil).head
    //    }
    override def pure[A](x: A): MoneyTree[A] = MoneyZLeaf(x)
  }
}
