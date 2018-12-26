package app.fmgp.money

import cats.kernel.Eq

object CurrencyY {
  implicit val eqv: Eq[CY] = Eq.fromUniversalEquals

  abstract sealed class CY(name: String)

  object XCY {
    def apply(name: String = "XXX"): CY = new CY(name) {}
    def empty = apply()
  }

  case object XXX extends CY("XXX") //Denote a "transaction" involving no currency.
  case object XTS extends CY("XTS") //Reserved for use in testing.
  //case object XBT extends CY("XBT") //Cryptocurrency Bitcoin but is not ISO 4217 approved
  case object XAU extends CY("XAU") //GOLD
  case object USD extends CY("USD") //
  case object GBP extends CY("GBP") //
  case object EUR extends CY("EUR") //
  //TODO ... ISO-4217

  //  class FFF(name: String) extends CY(name)
  //  case object FFF extends FFF("XXX0")
  //  case object FFF1 extends FFF("XXX1")
  //  case object FFF2 extends FFF("XXX2")
}

trait Currency {
  abstract sealed class CCC(name: String)
  case object AED extends CCC("AED")
  case object AUD extends CCC("AUD")
  case object BWP extends CCC("BWP")
  case object CAD extends CCC("CAD")
  case object CHF extends CCC("CHF")
  case object EUR extends CCC("EUR")
  case object GBP extends CCC("GBP")
  case object JPY extends CCC("JPY") //was not minor type *1
  case object LYD extends CCC("LYD") //Minor type *1000
  case object MAD extends CCC("MAD")
  case object MUR extends CCC("MUR")
  case object NAD extends CCC("NAD")
  case object NZD extends CCC("NZD")
  case object OMR extends CCC("OMR")
  case object THB extends CCC("THB")
  case object USD extends CCC("USD")
  case object ZAR extends CCC("ZAR")
  case object FJD extends CCC("FJD")
}
