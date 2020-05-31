package app.fmgp.money.experimental

import cats.instances.bigDecimal.catsKernelStdGroupForBigDecimal
import cats.instances.map.catsKernelStdMonoidForMap
import cats.syntax.semigroup.catsSyntaxSemigroup

case class MoneyZ[T](amount: BigDecimal) //This must be invariant in type T to correctly support monoidK

object MoneyK {
  def empty[K] = MoneyK[K](Map())
  implicit def eqv[K]: cats.kernel.Eq[MoneyK[K]] = cats.kernel.Eq.fromUniversalEquals
}

case class MoneyK[K](value: scala.collection.immutable.Map[K, BigDecimal]) extends AnyVal {

  private def update(k: K, f: BigDecimal => BigDecimal) = value.updated(k, f(value.getOrElse(k, 0)))
  //def +(k: K, v: BigDecimal): MoneyK[K] =
  def +(k: K, v: BigDecimal): MoneyK[K] = MoneyK[K](value |+| Map(k -> v)) //MoneyK[K](update(k, _ + v))
  def ++(other: MoneyK[K]): MoneyK[K] = MoneyK[K](value |+| other.value)
}
