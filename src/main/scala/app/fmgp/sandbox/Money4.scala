package app.fmgp.sandbox

import app.fmgp.sandbox.CurrencyX.CurrencyX
import cats.Eq
import cats.kernel.CommutativeGroup


sealed abstract case class Money4[+T <: CurrencyX](amount: BigDecimal) {
  def currency: T
  def currencyName = currency.toString
  def toMoney4: Money4Map = Money4Map.fromMoney(this) //FUCK YEAH !! +T
}

object Money4 {
  def apply[T <: CurrencyX](amount: BigDecimal, t: T): Money4[T] = new Money4[T](amount) { //FIXME need to round
    override def currency: T = t
  }
  implicit val eqv: Eq[Money4[_]] = Eq.fromUniversalEquals
}


sealed abstract case class Money4Map(w: Map[CurrencyX, BigDecimal])


object Money4MapCommutativeGroup {
  implicit val Money4CommutativeGroup: CommutativeGroup[Money4Map] = new CommutativeGroup[Money4Map] {

    import cats.instances.bigDecimal._
    import cats.instances.map._
    import cats.syntax.monoid._

    override def combine(x: Money4Map, y: Money4Map): Money4Map = Money4Map(x.w |+| y.w)
    override def empty: Money4Map = Money4Map.empty

    import cats.instances.bigDecimal.catsKernelStdGroupForBigDecimal

    override def inverse(a: Money4Map): Money4Map = Money4Map(a.w.mapValues(e => catsKernelStdGroupForBigDecimal.inverse(e)))
  }
}

object Money4Map {
  def empty: Money4Map = new Money4Map(Map[CurrencyX, BigDecimal]()) {}
  def apply(w: Map[CurrencyX, BigDecimal]): Money4Map = new Money4Map(w) {} //TODO make this protected in package
  def fromMoney(m: Money4[CurrencyX]): Money4Map = {
    if (m.amount == BigDecimal(0)) empty
    else new Money4Map(Map[CurrencyX, BigDecimal](m.currency -> m.amount)) {}
  }

  import cats.instances.bigDecimal.catsKernelStdGroupForBigDecimal

  implicit val eqv: Eq[Money4Map] = Eq.instance((a, b) =>
    a.w.filterNot(_._2 == catsKernelStdGroupForBigDecimal.empty) == b.w.filterNot(_._2 == catsKernelStdGroupForBigDecimal.empty)
  )
}
