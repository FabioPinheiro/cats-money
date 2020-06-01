package app.fmgp.money.experimental

import app.fmgp.money._
import cats.{MonoidK, Show}
import cats.kernel.{Monoid, Order}
import cats.syntax.monoid._
import cats.instances.bigDecimal._

import scala.language.implicitConversions

trait MoneyInstancesK {
  given MonoidK[MoneyK] = new MonoidK[MoneyK] {
    override def empty[K]: MoneyK[K] = MoneyK[K](Map())
    override def combineK[K](x: MoneyK[K], y: MoneyK[K]): MoneyK[K] = x ++ y
  }
}

class MoneyZMonoidK extends MonoidK[MoneyZ] {
  override def empty[A]: MoneyZ[A] = MoneyZ[A](0)
  override def combineK[A](x: MoneyZ[A], y: MoneyZ[A]): MoneyZ[A] = MoneyZ[A](x.amount |+| y.amount)
}