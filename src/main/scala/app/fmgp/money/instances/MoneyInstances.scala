package app.fmgp.money.instances

import app.fmgp.money._
//import app.fmgp.money.instances.MoneyInstances.MoneyZWithTag
import cats.{MonoidK, Show}
import cats.kernel.{Monoid, Order}
import cats.syntax.monoid._
import cats.instances.bigDecimal._

import scala.language.implicitConversions


trait MoneyInstances {
  given MoneyShow [T] (using sss: Show[T]) as Show[Money[T]] {
    def show(money: Money[T]) = "" + money.amount + " " + sss.show(money.currency)
  }
  implicit def MoneyMonoidT[T](c: T): Monoid[Money[T]] = new MoneyMonoid[T](c)
}

/*  given Monoid[Money[CURRENCY]] {
    def (x: Money[CURRENCY]) combine (y: Money[CURRENCY]): Money[CURRENCY] = Money(x.amount |+| y.amount, x.currency)
    def empty: Money[CURRENCY] = Money[CURRENCY](0, USD)
  }*/
class MoneyMonoid[C](c: C) extends Monoid[Money[C]] {
  override def combine(x: Money[C], y: Money[C]): Money[C] = {
    assert(x.currency == y.currency, s"Can not combine two different currencies '${x.currency}' and '${y.currency}'")
    Money(x.amount |+| y.amount, x.currency)
  }
  override def empty: Money[C] = Money[C](0, c)
}

trait MoneyInstancesC[CURRENCY] extends MoneyInstances {
  implicit def monoidMoneyCWithPartialCollector[C <: CURRENCY](
      c: C,
      rc: PartialRateConverter[CURRENCY, C]
  ): Monoid[Money[CURRENCY]] = new MoneyMonoidC(c, rc)

  given moneyOrder[C <: CURRENCY] as Order[Money[C]] = new Order[Money[C]] {
    override def compare(x: Money[C], y: Money[C]): Int = //Like in Order.by{_.amount}
      cats.instances.bigDecimal.catsKernelStdOrderForBigDecimal.compare(x.amount, y.amount)
  }
}

class MoneyMonoidC[CURRENCY, C <: CURRENCY](
    c: C,
    partialRateConverter: PartialRateConverter[CURRENCY, C]
) extends Monoid[Money[CURRENCY]] {

  lazy val monoidOfManeyC: MoneyMonoid[CURRENCY] = new MoneyMonoid[CURRENCY](c)
  given MoneyMonoid[CURRENCY] = monoidOfManeyC

  override def combine(x: Money[CURRENCY], y: Money[CURRENCY]): Money[CURRENCY] =
    monoidOfManeyC.combine(partialRateConverter.convert(x), partialRateConverter.convert(y))
  override def empty: Money[CURRENCY] = Money[CURRENCY](0, c)
}
