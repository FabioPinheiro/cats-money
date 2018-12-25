package app.fmgp.money

import app.fmgp.money.CurrencyY.CY
import cats.kernel.{Eq, Monoid, Order}

sealed abstract case class MoneyY[+T <: CY](amount: BigDecimal, currency: T)

object MoneyY {
  def apply[T <: CY](amount: BigDecimal, t: T): MoneyY[T] = new MoneyY[T](amount, t) {} //TODO need to round
  def fromTuple[T <: CY](m: (T, BigDecimal)): MoneyY[T] = MoneyY.apply(m._2, m._1)
  //def zero[T <: CurrencyY.CY](t: T): MoneyY[T] = new MoneyY[T](BigDecimal(0), t) {}
  implicit def eqv[T <: CY]: Eq[MoneyY[T]] = Eq.fromUniversalEquals
}

object MoneyYMonoid {

  import cats.syntax.monoid._
  import cats.instances.bigDecimal._

  implicit val MoneyOrder: Order[MoneyY[_]] = Order.by(_.amount)

  //import scala.language.implicitConversions
  def fMoneyYMonoid[C <: CY](c: C): Monoid[MoneyY[C]] = new Monoid[MoneyY[C]] {
    override def combine(x: MoneyY[C], y: MoneyY[C]): MoneyY[C] = MoneyY(x.amount |+| y.amount, x.currency)
    override def empty: MoneyY[C] = MoneyY[C](0, c)
  }

  implicit def monoidMoneyCYWithUnsafeCollector[C <: CY](c: C, unsafeCollector: UnsafeRateConverter[C]): Monoid[MoneyY[CY]] = new Monoid[MoneyY[CY]] {
    implicit val monoidOfManeyC = fMoneyYMonoid(c)
    override def combine(x: MoneyY[CY], y: MoneyY[CY]): MoneyY[CY] = //FIXME cast
      monoidOfManeyC.combine(unsafeCollector.convert(x), unsafeCollector.convert(y)).asInstanceOf[MoneyY[CY]]

    override def empty: MoneyY[CY] = MoneyY[CY](0, c)
  }
}
