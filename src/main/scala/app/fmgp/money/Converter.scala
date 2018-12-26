package app.fmgp.money

case class PartialRateConverter[C, T <: C](pf: PartialFunction[MoneyY[C], MoneyY[T]]) {
  /** @throws MatchError case a conversion rate is missing () */
  def convert(money: MoneyY[C]): MoneyY[T] = pf(money)
  //.applyOrElse(money, throw new MatchError(s"Missing conversion rate for ${money.currency}")) //TODO throws error
}

object PartialRateConverter {
  def fromMapRates[C, T <: C](to: T, rates: Map[C, BigDecimal]) = {
    val ii = rates.filterNot(_._1 == to).map {
      case (from, rate) =>
        val pf: PartialFunction[MoneyY[C], MoneyY[T]] = {
          case MoneyY(amount, `from`) => MoneyY[T](amount * rate, to)
        }
        pf
    }
    val pf: PartialFunction[MoneyY[C], MoneyY[T]] = {
      case m@MoneyY(_, `to`) => m.asInstanceOf[MoneyY[T]] //TODO THIS IS A SAFE CAST
    }
    PartialRateConverter(pf orElse ii.reduce((a, b) => a orElse b))
  }
  def fromPFRates[C, T <: C](to: T, rates: PartialFunction[C, BigDecimal]) = {
    new PartialFunction[MoneyY[C], MoneyY[T]] {
      override def isDefinedAt(x: MoneyY[C]): Boolean = rates.isDefinedAt(x.currency)
      override def apply(v1: MoneyY[C]): MoneyY[T] = {
        val rate = rates(v1.currency)
        MoneyY[T](v1.amount * rate, to)
      }
    }
  }
}
