package app.fmgp.money

import cats.kernel.Eq

sealed abstract case class MoneyY[+T](amount: BigDecimal, currency: T)

object MoneyY {
  def apply[T](amount: BigDecimal, t: T): MoneyY[T] = new MoneyY[T](amount, t) {}
  def fromTuple[T](m: (T, BigDecimal)): MoneyY[T] = MoneyY.apply(m._2, m._1)
  implicit def eqv[T]: Eq[MoneyY[T]] = Eq.fromUniversalEquals
}

case class MoneyZ[T](amount: BigDecimal) //This must be invariant in type T to correctly support monoidK
