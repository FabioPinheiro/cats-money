package app.fmgp.money

import cats.Functor

case class PartialRateConverter[C, T <: C](pf: PartialFunction[MoneyY[C], MoneyY[T]]) {
  /** @throws MatchError case a conversion rate is missing () */
  def convert(money: MoneyY[C]): MoneyY[T] = apply(money)
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


///** @throws MatchError case a conversion rate is missing () */
//sealed abstract class PartialRateConverter[C, T <: C] extends PartialFunction[MoneyY[C], MoneyY[T]] {
//  def convert(money: MoneyY[C]): MoneyY[T] = apply(money)
//  //applyOrElse(money, throw new MatchError(s"Missing conversion rate for ${money.currency}"))
//}
//
//object PartialRateConverter {

//
//  def fromMapRates[C, T <: C](to: T, rates: Map[C, BigDecimal]): PartialRateConverter[C, T] = {
//    val it = rates.filterNot(_._1 == to).map {
//      case (from, rate) => new PartialFunction[MoneyY[C], MoneyY[T]] {
//        override def isDefinedAt(x: MoneyY[C]): Boolean = x.currency == from
//        override def apply(v1: MoneyY[C]): MoneyY[T] = MoneyY[T](v1.amount * rate, to)
//      }
//    }
//    new PartialRateConverter[C, T] {
//      override def isDefinedAt(v: MoneyY[C]): Boolean = v.currency == to || rates.keys.toSeq.contains(v.currency)
//      override def apply(v: MoneyY[C]): MoneyY[T] = (tToT[C, T](to) orElse it.reduce((a, b) => a orElse b)).apply(v)
//    }
//  }
//
//  def fromPFRates[C, T <: C](to: T, rates: PartialFunction[C, BigDecimal]): PartialRateConverter[C, T] = new PartialRateConverter[C, T] {
//    override def isDefinedAt(v: MoneyY[C]): Boolean = v.currency == to || rates.isDefinedAt(v.currency)
//    override def apply(v: MoneyY[C]): MoneyY[T] = v match {
//      case MoneyY(a, `to`) => MoneyY[T](a, to)
//      case MoneyY(amount, currency) => MoneyY[T](amount * rates(currency), to)
//    }
//  }
//}
