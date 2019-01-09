package app.fmgp.money

import cats.kernel.Eq

sealed abstract case class MoneyY[+T](amount: BigDecimal, currency: T)

object MoneyY {
  def apply[T](amount: BigDecimal, t: T): MoneyY[T] = new MoneyY[T](amount, t) {}
  def fromTuple[T](m: (T, BigDecimal)): MoneyY[T] = MoneyY.apply(m._2, m._1)
  implicit def eqv[T]: Eq[MoneyY[T]] = Eq.fromUniversalEquals
}

case class MoneyZ[T](amount: BigDecimal) //This must be invariant in type T to correctly support monoidK

object MoneyK {
  def empty[K] = MoneyK[K](Map())
  implicit def eqv[K]: Eq[MoneyK[K]] = Eq.fromUniversalEquals
}

case class MoneyK[K](value: scala.collection.immutable.Map[K, BigDecimal]) extends AnyVal {

  import cats.syntax.semigroup._
  import cats.instances.bigDecimal._
  import cats.instances.map._

  private def update(k: K, f: BigDecimal => BigDecimal) = value.updated(k, f(value.getOrElse(k, 0)))
  //def +(k: K, v: BigDecimal): MoneyK[K] =
  def +(k: K, v: BigDecimal): MoneyK[K] = MoneyK[K](value |+| Map(k -> v)) //MoneyK[K](update(k, _ + v))
  def ++(other: MoneyK[K]): MoneyK[K] = MoneyK[K](value |+| other.value)
}
