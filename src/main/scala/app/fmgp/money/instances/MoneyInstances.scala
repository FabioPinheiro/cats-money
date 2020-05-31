package app.fmgp.money.instances

import app.fmgp.money._
//import app.fmgp.money.instances.MoneyInstances.MoneyZWithTag
import cats.{MonoidK, Show}
import cats.kernel.{Monoid, Order}
import cats.syntax.monoid._
import cats.instances.bigDecimal._

import scala.language.implicitConversions


trait MoneyInstances {
  given moneyYShow [T] (using sss: Show[T]) as Show[MoneyY[T]] {
    def show(money: MoneyY[T]) = "" + money.amount + " " + sss.show(money.currency)
  }
  implicit def moneyYMonoidT[T](c: T): Monoid[MoneyY[T]] = new MoneyYMonoid[T](c)
}

class MoneyYOrder[T, C <: T] extends Order[MoneyY[C]] {
  override def compare(x: MoneyY[C], y: MoneyY[C]): Int = //Like in Order.by{_.amount}
    cats.instances.bigDecimal.catsKernelStdOrderForBigDecimal.compare(x.amount, y.amount)
}
/*  given Monoid[MoneyY[CURRENCY]] {
    def (x: MoneyY[CURRENCY]) combine (y: MoneyY[CURRENCY]): MoneyY[CURRENCY] = MoneyY(x.amount |+| y.amount, x.currency)
    def empty: MoneyY[CURRENCY] = MoneyY[CURRENCY](0, USD)
  }*/
class MoneyYMonoid[C](c: C) extends Monoid[MoneyY[C]] {
  override def combine(x: MoneyY[C], y: MoneyY[C]): MoneyY[C] = {
    assert(x.currency == y.currency, s"Can not combine two different currencies '${x.currency}' and '${y.currency}'")
    MoneyY(x.amount |+| y.amount, x.currency)
  }
  override def empty: MoneyY[C] = MoneyY[C](0, c)
}

trait MoneyInstancesC[CURRENCY] extends MoneyInstances {
  //### MoneyY ###
  implicit def monoidMoneyCWithPartialCollector[C <: CURRENCY](
      c: C,
      rc: PartialRateConverter[CURRENCY, C]
  ): Monoid[MoneyY[CURRENCY]] = new MoneyMonoidC(c, rc)
  implicit def moneyOrder[C <: CURRENCY]: Order[MoneyY[C]] = new MoneyYOrder[CURRENCY, C]
}

class MoneyMonoidC[CURRENCY, C <: CURRENCY](
    c: C,
    partialRateConverter: PartialRateConverter[CURRENCY, C]
) extends Monoid[MoneyY[CURRENCY]] {

  lazy val monoidOfManeyC: MoneyYMonoid[CURRENCY] = new MoneyYMonoid[CURRENCY](c)
  given MoneyYMonoid[CURRENCY] = monoidOfManeyC

  override def combine(x: MoneyY[CURRENCY], y: MoneyY[CURRENCY]): MoneyY[CURRENCY] =
    monoidOfManeyC.combine(partialRateConverter.convert(x), partialRateConverter.convert(y))
  override def empty: MoneyY[CURRENCY] = MoneyY[CURRENCY](0, c)
}
