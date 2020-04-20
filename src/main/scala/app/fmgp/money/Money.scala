package app.fmgp.money

import cats.instances.bigDecimal.catsKernelStdGroupForBigDecimal
import cats.instances.map.catsKernelStdMonoidForMap
import cats.kernel.Eq
import cats.syntax.semigroup.catsSyntaxSemigroup

sealed abstract case class MoneyY[+T](amount: BigDecimal, currency: T)

object MoneyY {
  def apply[T](amount: BigDecimal, t: T): MoneyY[T] = new MoneyY[T](amount, t) {}
  def fromTuple[T](m: (T, BigDecimal)): MoneyY[T] = MoneyY.apply(m._2, m._1)
  implicit def eqv[T]: Eq[MoneyY[T]] = Eq.fromUniversalEquals
}

import app.fmgp.money.MoneyTree
import cats.{Applicative, Eval, Functor, Monad, Show, Traverse}

import cats.Monad
import cats.data.Writer
import cats.kernel.{Eq, Monoid}

sealed abstract class MoneyTree[+T] {
  def collapse: MoneyTree[T]
  def collectValues: Seq[T]
  def simplify[S >: T](implicit monoid: Monoid[S]): MoneyTree[S] =
    MoneyTree.leafs[S](monoid.combineAll(this.collectValues: TraversableOnce[T]))
  def :+[S >: T](a: S): MoneyTree[S] = ???
}

object MoneyTree {
  def empty[T]: MoneyTree[T] = ??? //MoneyZBranch[T](Seq.empty)
  def one[T](value: T): MoneyTree[T] = ??? //MoneyZLeaf[T](value)
  def branch[T](m: Seq[MoneyTree[T]]): MoneyTree[T] = ??? ///MoneyZBranch[T](m)
  def join[T](m: MoneyTree[T]*): MoneyTree[T] = ??? ///MoneyZBranch[T](m)
  def leafs[T](m: T*): MoneyTree[T] = ??? ///MoneyZBranch[T](m.map(one))

  implicit def eqTree[T: Eq]: Eq[MoneyTree[T]] = ??? //Eq.fromUniversalEquals
}

object MoneyTreeInstances {
  implicit def MoneyTreeShow[T](implicit showT: Show[T]): Show[MoneyTree[T]] = ???
  implicit val MoneyTreeFunctor: Functor[MoneyTree] = ???
  implicit val MoneyTreeMonad: Monad[MoneyTree] = ???
  val MoneyTreeTraverse: Traverse[MoneyTree] = ???
}

// object Demo extends App {
//   import MoneyTreeInstances._
//   val a = MoneyY(100, Int)
//   val b = MoneyY(200, String)

//   type SomeUnionType = Int | Unit
//   //FIXME
//   val maybeBug: MoneyTree[MoneyY[SomeUnionType]] = (a: SomeUnionType).pure[MoneyTree] :+ b // :+ c :+ d

// }
