package app.fmgp.money

import cats.kernel.Eq

/** TODO ISO-4217 */
object CurrencyY {
  implicit val eqv: Eq[CY] = Eq.fromUniversalEquals

  type CurrencyY = CY
  abstract sealed class CY(name: String)
  //val CYValues: Seq[CY] = Seq(XXX, XTS, XAU, USD, GBP, EUR) //for test
  val CYValues: Seq[CY] = Seq(USD, GBP, EUR) //for test


  object XCY {
    def apply(name: String = "XXX"): CY = new CY(name) {}
    def empty = apply()
  }

  class FFF(name: String) extends CY(name) //SUPPORT & TEST Minor TYPES
  case object FFF extends FFF("XXX0")
  case object FFF1 extends FFF("XXX1")
  case object FFF2 extends FFF("XXX2")

  case object XXX extends CY("XXX") //Denote a "transaction" involving no currency.
  case object XTS extends CY("XTS") //Reserved for use in testing.
  //case object XBT extends CY("XBT") //Cryptocurrency Bitcoin but is not ISO 4217 approved
  case object XAU extends CY("XAU") //GOLD

  case object USD extends CY("USD") //
  case object GBP extends CY("GBP") //
  case object EUR extends CY("EUR") //

  //TODO support Minors types
}
