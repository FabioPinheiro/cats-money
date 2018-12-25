package app.fmgp.sandbox

import cats.implicits._
import cats.instances.bigDecimal.catsKernelStdGroupForBigDecimal
import cats.kernel._


sealed trait CC {
  def name: String
}

case object CCUSD extends CC {
  def name: String = "USD"
}

case object CCEUR extends CC {
  def name: String = "EUR"
}


trait Money2B[T <: CC] {
  def amount: BigDecimal
  def currency: T
  def currencyName = currency.name
  def toMoney2Map: Money2Map = Money2Map(Map(currency -> amount))
}

case class Money2USD(amount2: BigDecimal) extends Money2B[CCUSD.type] {
  override def amount: BigDecimal = amount2
  def currency = CCUSD
}
case class Money2EUR(amount2: BigDecimal) extends Money2B[CCEUR.type] {
  override def amount: BigDecimal = amount2
  def currency = CCEUR
}

case class Money2Map(w: Map[CC, BigDecimal])
object Money2Map {
  //implicit val eq: Eq[Money2Map] = Eq.fromUniversalEquals //DON"T WORK!
  implicit val eqv: Eq[Money2Map] = Eq.instance((a, b) =>
    a.w.filterNot(_._2 == catsKernelStdGroupForBigDecimal.empty) == b.w.filterNot(_._2 == catsKernelStdGroupForBigDecimal.empty)
  )
}

object Money2Monoid {


  implicit val monoid: Monoid[Money2Map] = new Monoid[Money2Map] {
    //import cats.implicits.catsKernelStdMonoidForMap
    override def combine(x: Money2Map, y: Money2Map): Money2Map = Money2Map((x.w |+| y.w))
    override def empty: Money2Map = Money2Map(Map.empty)
  }
}


object Money2Group {
  implicit val group: Group[Money2Map] = new Group[Money2Map] {
    override def combine(x: Money2Map, y: Money2Map): Money2Map = Money2Map(x.w |+| y.w)
    override def empty: Money2Map = Money2Map(Map.empty)

    import cats.instances.bigDecimal.catsKernelStdGroupForBigDecimal

    override def inverse(a: Money2Map): Money2Map = Money2Map(a.w.mapValues(e => catsKernelStdGroupForBigDecimal.inverse(e)))
    override def remove(a: Money2Map, b: Money2Map): Money2Map = super.remove(a, b)
  }
}

