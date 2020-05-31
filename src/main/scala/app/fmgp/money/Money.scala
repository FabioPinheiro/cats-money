package app.fmgp.money

import cats.instances.bigDecimal.catsKernelStdGroupForBigDecimal
import cats.instances.map.catsKernelStdMonoidForMap
import cats.syntax.semigroup.catsSyntaxSemigroup

sealed abstract case class MoneyY[+T](amount: BigDecimal, currency: T) derives Eql

object MoneyY {
  def apply[T](amount: BigDecimal, t: T): MoneyY[T] = new MoneyY[T](amount, t) {}
  def fromTuple[T](m: (T, BigDecimal)): MoneyY[T] = MoneyY.apply(m._2, m._1)
  //implicit def eqv[T]: cats.kernel.Eq[MoneyY[T]] = cats.kernel.Eq.fromUniversalEquals
}