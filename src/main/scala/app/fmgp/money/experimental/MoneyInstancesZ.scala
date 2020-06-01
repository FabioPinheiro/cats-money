package app.fmgp.money.experimental

import app.fmgp.money._
//import app.fmgp.money.instances.MoneyInstances.MoneyZWithTag
import cats.{MonoidK, Show}
import cats.kernel.{Monoid, Order}
import cats.syntax.monoid._
import cats.instances.bigDecimal._

import scala.language.implicitConversions

// object MoneyInstances {
//   //trait MyTag
//   //type MoneyZWithTag[A] = MoneyZ[A] with MyTag
//   //def ring[A](x: MoneyZ[A]): MoneyZWithTag[A] = x.asInstanceOf[MoneyZWithTag[A]]
//   //def ring[A](c: A, x: BigDecimal): MoneyZWithTag[A] = MoneyZ[A](x).asInstanceOf[MoneyZWithTag[A]]
// }

trait MoneyInstancesZ {

  //### MoneyZ ###
  // /** Add a new method currency */
  // implicit class MoneyZWithCompanion[T](value: MoneyZ[T]) {
  //   def currency(implicit companion: Companion[T]) = companion.apply()
  // }

  // given moneyZShow [T] (using companionT: Companion[T]) as Show[MoneyZ[T]] {
  //   def show(money: MoneyZ[T]) = "" + money.amount + " " + money.currency
  // }
  // // given moneyZShowWithTag [T] (using companionT: Companion[T]) as Show[MoneyZWithTag[T]] {
  // //   def show(money: MoneyZWithTag[T]) = "" + money.amount + " TAGGED-" + money.currency
  // // }
  // implicit def moneyZMonoidK[T]: MonoidK[MoneyZ] = new MoneyZMonoidK
  // //implicit def moneyZMonoidKMultiplication[T]: MonoidK[MoneyZWithTag] = new MoneyZMonoidKWithTag
  
  given MonoidK[MoneyZ] = new MonoidK[MoneyZ] {
    override def empty[A]: MoneyZ[A] = MoneyZ[A](0)
    override def combineK[A](x: MoneyZ[A], y: MoneyZ[A]): MoneyZ[A] = MoneyZ[A](x.amount |+| y.amount)
  }
}


// class MoneyZMonoidKWithTag extends MonoidK[MoneyZWithTag] {
//   override def empty[A]: MoneyZWithTag[A] = MoneyZ[A](0).asInstanceOf[MoneyZWithTag[A]]
//   override def combineK[A](x: MoneyZWithTag[A], y: MoneyZWithTag[A]): MoneyZWithTag[A] =
//     MoneyZ[A](x.amount * y.amount).asInstanceOf[MoneyZWithTag[A]]
// }
