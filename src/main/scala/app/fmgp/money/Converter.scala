package app.fmgp.money

trait Converter[FROM, TO] {
  def convert(from: FROM): TO
}

//TODO REMOVE THIS WILL NOT WORK
case class ShapelessConverter[T <: EUR_XXX.type](eur: Double, xxx: Double) extends Converter[MoneyY[EUR_XXX.type], MoneyY[T]] {

  object poly extends shapeless.Poly1 {
    implicit def caseEUR = at[CurrencyY.EUR.type](s => eur)
    implicit def caseXXX = at[CurrencyY.XXX.type](s => xxx)
  }

  def convertTODO(money: MoneyY[CurrencyY.EUR.type]): MoneyY[T] = {
    val pf = poly(money.currency)
    ??? //MoneyY(money.amount * pf, T)
  }

  def convert(money: MoneyY[EUR_XXX.type]): MoneyY[T] = ???
}

case class PartialRateConverter[C, T <: C](pf: PartialFunction[MoneyY[C], MoneyY[T]]) extends Converter[MoneyY[C], MoneyY[T]] {
  /** @throws MatchError case a conversion rate is missing () */
  def convert(money: MoneyY[C]): MoneyY[T] = apply(money)
  def convertOption(money: MoneyY[C]): Option[MoneyY[T]] = if (isDefinedAt(money)) Some(apply(money)) else None
  def isDefinedAt(money: MoneyY[C]): Boolean = pf.isDefinedAt(money)
  //TODO def isDefinedAt[F[_]](money: F[MoneyY[C]])(f: Functor[F]): Boolean = f.map(money)(e => isDefinedAt(e))
  def apply(money: MoneyY[C]): MoneyY[T] = pf(money)
}

object PartialRateConverter {
  def tToT[C, T](to: T): PartialFunction[MoneyY[C], MoneyY[T]] = {
    case MoneyY(a, `to`) => MoneyY[T](a, to) //m.asInstanceOf[MoneyY[T]] //THIS WAS A SAFE CAST
  }

  def fromMapRates[C, T <: C](to: T, rates: Map[C, BigDecimal]) = {
    val ii = rates.filterNot(_._1 == to).map {
      case (from, rate) =>
        val pf: PartialFunction[MoneyY[C], MoneyY[T]] = {
          case MoneyY(amount, `from`) => MoneyY[T](amount * rate, to)
        }
        pf
    }
    PartialRateConverter(tToT[C, T](to) orElse ii.reduce((a, b) => a orElse b))
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
