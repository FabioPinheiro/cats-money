package app.fmgp

/**
 * .
 * .   EUR USD ...
 * .    |   |                 MoneyY[_]               MoneyTree[_]
 * .    |   |                    |                         |
 * .    Currency:                |                         |
 * .  (CurrencyY.CY) -------> MoneyY[Currency]        Monad[MoneyTree]
 * .
 * .                                    Monoid[MoneyY[C <: CY]]
 * .                                    Monoid[MoneyY[CY]](with PartialRateConverter)
 * .
 * . PartialRateConverter[T,C<:T]
 * .      (T like CY)
 * .
 * .  Converter[Currency.CY]
 */
package object money {

}
