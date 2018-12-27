package app.fmgp.money.instances

import app.fmgp.money.{MoneyY, PartialRateConverter}
import cats.kernel.{Monoid, Order}
import cats.syntax.monoid._
import cats.instances.bigDecimal._
import scala.language.implicitConversions

trait MoneyInstances {
  implicit def moneyYMonoidT[T](c: T): Monoid[MoneyY[T]] = new MoneyMonoid[T](c)
}

class moneyOrder[T, C <: T] extends Order[MoneyY[C]] {
  override def compare(x: MoneyY[C], y: MoneyY[C]): Int = //Like in Order.by{_.amount}
    cats.instances.bigDecimal.catsKernelStdOrderForBigDecimal.compare(x.amount, y.amount)
}

class MoneyMonoid[C](c: C) extends Monoid[MoneyY[C]] {
  override def combine(x: MoneyY[C], y: MoneyY[C]): MoneyY[C] = {
    assert(x.currency == y.currency, s"Can not combine two different currencies '${x.currency}' and '${y.currency}'")
    MoneyY(x.amount |+| y.amount, x.currency)
  }
  override def empty: MoneyY[C] = MoneyY[C](0, c)
}

// With CURRENCY

trait MoneyInstancesC[CURRENCY] extends MoneyInstances {
  implicit def monoidMoneyCWithPartialCollector[C <: CURRENCY](c: C, rc: PartialRateConverter[CURRENCY, C]): Monoid[MoneyY[CURRENCY]] = new MoneyMonoidC(c, rc)
  implicit def moneyOrder[C <: CURRENCY]: Order[MoneyY[C]] = new moneyOrder[CURRENCY, C]
}

class MoneyMonoidC[CURRENCY, C <: CURRENCY](c: C, partialRateConverter: PartialRateConverter[CURRENCY, C]) extends Monoid[MoneyY[CURRENCY]] {
  implicit val monoidOfManeyC: MoneyMonoid[CURRENCY] = new MoneyMonoid[CURRENCY](c)
  override def combine(x: MoneyY[CURRENCY], y: MoneyY[CURRENCY]): MoneyY[CURRENCY] = monoidOfManeyC.combine(partialRateConverter(x), partialRateConverter(y))
  override def empty: MoneyY[CURRENCY] = MoneyY[CURRENCY](0, c)
}



