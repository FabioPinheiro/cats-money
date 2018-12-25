package app.fmgp.money

import app.fmgp.money.CurrencyY._

case class UnsafeRateConverter[T <: CurrencyY](pf: PartialFunction[MoneyY[CurrencyY], MoneyY[T]]) {
  /** @throws MatchError case a conversion rate is missing () */
  def convert(money: MoneyY[CurrencyY]): MoneyY[T] = pf(money)
  //.applyOrElse(money, throw new MatchError(s"Missing conversion rate for ${money.currency}")) //TODO throws error
}

object UnsafeRateConverter {
  def fromMapRates[T <: CurrencyY](to: T, rates: Map[CurrencyY, BigDecimal]) = {
    val ii = rates.filterNot(_._1 == to).map {
      case (from, rate) =>
        val pf: PartialFunction[MoneyY[CurrencyY], MoneyY[T]] = {
          case MoneyY(amount, `from`) =>
            MoneyY[T](amount * rate, to)
        }
        pf
    }
    val pf: PartialFunction[MoneyY[CurrencyY], MoneyY[T]] = {
      case m@MoneyY(_, `to`) => m.asInstanceOf[MoneyY[T]] //TODO THIS IS A SAFE CAST
    }
    UnsafeRateConverter(pf orElse ii.reduce((a, b) => a orElse b))
  }
  def fromPFRates[T <: CurrencyY](to: T, rates: PartialFunction[CurrencyY, BigDecimal]) = {
    new PartialFunction[MoneyY[CurrencyY], MoneyY[T]] {
      override def isDefinedAt(x: MoneyY[CurrencyY]): Boolean = rates.isDefinedAt(x.currency)
      override def apply(v1: MoneyY[CurrencyY]): MoneyY[T] = {
        val rate = rates(v1.currency)
        MoneyY[T](v1.amount * rate, to)
      }
    }
  }
}
