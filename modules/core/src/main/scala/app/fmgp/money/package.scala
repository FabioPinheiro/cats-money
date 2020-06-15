package app.fmgp

package object money {
  // ### Precision Number ###
  // BigDecimal is being used to be more accurate during calculations
  type MoneyAmount = BigDecimal
  type RateValue = BigDecimal
}
