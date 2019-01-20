package app.fmgp.sandbox

import cats.implicits._
import cats.instances.bigDecimal.catsKernelStdGroupForBigDecimal
import cats.kernel.{CommutativeGroup, Eq}

trait Money3B[T <: CC] {
  def amount: BigDecimal
  def currency: T
  def currencyName = currency.name
  def toMoney2Map: Money2Map = Money2Map(Map(currency -> amount))
}

object Money3B {
  def apply[T <: CC](amount2: BigDecimal, t: T): Money3B[T] = new Money3B[T]() {
    override def amount: BigDecimal = amount2
    override def currency: T = t
  }
}

object Money3CommutativeGroup {
  implicit val group: CommutativeGroup[Money3Map] = new CommutativeGroup[Money3Map] {
    override def combine(x: Money3Map, y: Money3Map): Money3Map = Money3Map(x.w |+| y.w)
    override def empty: Money3Map = Money3Map(Map.empty)

    override def inverse(a: Money3Map): Money3Map = Money3Map(a.w.mapValues(e => catsKernelStdGroupForBigDecimal.inverse(e)))
  }
}

case class Money3Map(w: Map[CC, BigDecimal])
object Money3Map {
  //implicit val eq: Eq[Money3Map] = Eq.fromUniversalEquals
  implicit val eq: Eq[Money3Map] = Eq.instance((a, b) =>
    a.w.filterNot(_._2 == catsKernelStdGroupForBigDecimal.empty) == b.w.filterNot(_._2 == catsKernelStdGroupForBigDecimal.empty)
  )
}
