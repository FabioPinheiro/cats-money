package app.fmgp.sandbox

import cats.kernel.Eq

object CurrencyX extends Enumeration {
  implicit val eqv: Eq[CurrencyX] = Eq.fromUniversalEquals

  type CurrencyX = Value

  val XXX = Value("XXX") //Denote a "transaction" involving no currency.
  val XTS = Value("XTS") //Reserved for use in testing.
  //val XBT = Value("XBT") //Cryptocurrency Bitcoin but is not ISO 4217 approved
  val XAU = Value("XAU") //GOLD
  val EUR = Value("EUR")
  val GBP = Value("GBP")
  val USD = Value("USD")
}

