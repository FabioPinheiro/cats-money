package app.fmgp.money.experimental

import app.fmgp.money._
//import app.fmgp.money.instances.MoneyInstances.MoneyZWithTag
import cats.{MonoidK, Show}
import cats.kernel.{Monoid, Order}
import cats.syntax.monoid._
import cats.instances.bigDecimal._

import scala.language.implicitConversions

trait MoneyInstancesK {
  // //### MoneyK ###
  // implicit def moneyKMonoidK[K]: MonoidK[MoneyK] = new MoneyKMonoidK
}

class MoneyZMonoidK extends MonoidK[MoneyZ] {
  override def empty[A]: MoneyZ[A] = MoneyZ[A](0)
  override def combineK[A](x: MoneyZ[A], y: MoneyZ[A]): MoneyZ[A] = MoneyZ[A](x.amount |+| y.amount)
}

class MoneyKMonoidK extends MonoidK[MoneyK] {
  override def empty[K]: MoneyK[K] = MoneyK[K](Map())
  override def combineK[K](x: MoneyK[K], y: MoneyK[K]): MoneyK[K] = x ++ y
}
