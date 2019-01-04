package app.fmgp.money.instances

import app.fmgp.money.{Companion, MoneyY, MoneyZ, PartialRateConverter}
import cats.{MonoidK, Show}
import cats.kernel.{Monoid, Order}
import cats.syntax.monoid._
import cats.instances.bigDecimal._

import scala.language.implicitConversions

trait MoneyInstances {
  //### MoneyY ###
  implicit def moneyYShow[T]: Show[MoneyY[T]] = Show.show(money => money.amount + " " + money.currency)
  implicit def moneyYMonoidT[T](c: T): Monoid[MoneyY[T]] = new MoneyMonoid[T](c)

  //### MoneyZ ###
  /** Add a new method currency */
  implicit class MoneyWithCompanion[T](value: MoneyZ[T]) {
    def currency(implicit companion: Companion[T]) = companion.apply()
  }
  implicit def moneyZShow[T](implicit /*showT: Show[T],*/ companionT: Companion[T]): Show[MoneyZ[T]] =
    Show.show(money => money.amount + " " + money.currency)
  implicit def moneyZMonoidK[T]: MonoidK[MoneyZ] = new MoneyMonoidK
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

class MoneyMonoidK extends MonoidK[MoneyZ] {
  override def empty[A]: MoneyZ[A] = MoneyZ[A](0)
  override def combineK[A](x: MoneyZ[A], y: MoneyZ[A]): MoneyZ[A] = MoneyZ[A](x.amount |+| y.amount)
}

// With CURRENCY

trait MoneyInstancesC[CURRENCY] extends MoneyInstances {
  //### MoneyY ###
  implicit def monoidMoneyCWithPartialCollector[C <: CURRENCY](c: C, rc: PartialRateConverter[CURRENCY, C]): Monoid[MoneyY[CURRENCY]] = new MoneyMonoidC(c, rc)
  implicit def moneyOrder[C <: CURRENCY]: Order[MoneyY[C]] = new moneyOrder[CURRENCY, C]
}

class MoneyMonoidC[CURRENCY, C <: CURRENCY](c: C, partialRateConverter: PartialRateConverter[CURRENCY, C]) extends Monoid[MoneyY[CURRENCY]] {
  implicit val monoidOfManeyC: MoneyMonoid[CURRENCY] = new MoneyMonoid[CURRENCY](c)
  override def combine(x: MoneyY[CURRENCY], y: MoneyY[CURRENCY]): MoneyY[CURRENCY] =
    monoidOfManeyC.combine(partialRateConverter.convert(x), partialRateConverter.convert(y))
  override def empty: MoneyY[CURRENCY] = MoneyY[CURRENCY](0, c)
}



