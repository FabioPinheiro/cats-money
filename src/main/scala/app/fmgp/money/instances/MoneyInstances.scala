package app.fmgp.money.instances

import app.fmgp.money._
//import app.fmgp.money.instances.MoneyInstances.MoneyZWithTag
import cats.{MonoidK, Show}
import cats.kernel.{Monoid, Order}
import cats.syntax.monoid._
import cats.instances.bigDecimal._

import scala.language.implicitConversions

object MoneyInstances {
  trait MyTag
  //type MoneyZWithTag[A] = MoneyZ[A] with MyTag
  //def ring[A](x: MoneyZ[A]): MoneyZWithTag[A] = x.asInstanceOf[MoneyZWithTag[A]]
  //def ring[A](c: A, x: BigDecimal): MoneyZWithTag[A] = MoneyZ[A](x).asInstanceOf[MoneyZWithTag[A]]
}

trait MoneyInstances {
  //### MoneyY ###
  given moneyYShow [T] (using sss: Show[T]) as Show[MoneyY[T]] {
    def show(money: MoneyY[T]) = "" + money.amount + " " + sss.show(money.currency)
  }
  implicit def moneyYMonoidT[T](c: T): Monoid[MoneyY[T]] = new MoneyYMonoid[T](c)

  //### MoneyZ ###
  /** Add a new method currency */
  // implicit class MoneyZWithCompanion[T](value: MoneyZ[T]) {
  //   def currency(implicit companion: Companion[T]) = companion.apply()
  // }

  // given moneyZShow [T] (using companionT: Companion[T]) as Show[MoneyZ[T]] {
  //   def show(money: MoneyZ[T]) = "" + money.amount + " " + money.currency
  // }
  // given moneyZShowWithTag [T] (using companionT: Companion[T]) as Show[MoneyZWithTag[T]] {
  //   def show(money: MoneyZWithTag[T]) = "" + money.amount + " TAGGED-" + money.currency
  // }
  // implicit def moneyZMonoidK[T]: MonoidK[MoneyZ] = new MoneyZMonoidK
  // implicit def moneyZMonoidKMultiplication[T]: MonoidK[MoneyZWithTag] = new MoneyZMonoidKWithTag

  //### MoneyK ###
  // implicit def moneyKMonoidK[K]: MonoidK[MoneyK] = new MoneyKMonoidK
}

class MoneyYOrder[T, C <: T] extends Order[MoneyY[C]] {
  override def compare(x: MoneyY[C], y: MoneyY[C]): Int = //Like in Order.by{_.amount}
    cats.instances.bigDecimal.catsKernelStdOrderForBigDecimal.compare(x.amount, y.amount)
}

class MoneyYMonoid[C](c: C) extends Monoid[MoneyY[C]] {
  override def combine(x: MoneyY[C], y: MoneyY[C]): MoneyY[C] = {
    assert(x.currency == y.currency, s"Can not combine two different currencies '${x.currency}' and '${y.currency}'")
    MoneyY(x.amount |+| y.amount, x.currency)
  }
  override def empty: MoneyY[C] = MoneyY[C](0, c)
}

// class MoneyZMonoidK extends MonoidK[MoneyZ] {
//   override def empty[A]: MoneyZ[A] = MoneyZ[A](0)
//   override def combineK[A](x: MoneyZ[A], y: MoneyZ[A]): MoneyZ[A] = MoneyZ[A](x.amount |+| y.amount)
// }

// class MoneyZMonoidKWithTag extends MonoidK[MoneyZWithTag] {
//   override def empty[A]: MoneyZWithTag[A] = MoneyZ[A](0).asInstanceOf[MoneyZWithTag[A]]
//   override def combineK[A](x: MoneyZWithTag[A], y: MoneyZWithTag[A]): MoneyZWithTag[A] =
//     MoneyZ[A](x.amount * y.amount).asInstanceOf[MoneyZWithTag[A]]
// }

// class MoneyKMonoidK extends MonoidK[MoneyK] {
//   override def empty[K]: MoneyK[K] = MoneyK[K](Map())
//   override def combineK[K](x: MoneyK[K], y: MoneyK[K]): MoneyK[K] = x ++ y
// }

// With CURRENCY

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
