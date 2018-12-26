package app.fmgp.money

import app.fmgp.money.CurrencyY.CY
import cats.kernel.{Eq, Monoid, Order}

sealed abstract case class MoneyY[+T](amount: BigDecimal, currency: T)

object MoneyY {
  def apply[T](amount: BigDecimal, t: T): MoneyY[T] = new MoneyY[T](amount, t) {}
  def fromTuple[T <: CY](m: (T, BigDecimal)): MoneyY[T] = MoneyY.apply(m._2, m._1)
  implicit def eqv[T <: CY]: Eq[MoneyY[T]] = Eq.fromUniversalEquals
}

object MoneyYMonoid {

  import cats.syntax.monoid._
  import cats.instances.bigDecimal._

  import scala.language.implicitConversions
  implicit def fMoneyYMonoid[T](c: T): Monoid[MoneyY[T]] = new Monoid[MoneyY[T]] {
    override def combine(x: MoneyY[T], y: MoneyY[T]): MoneyY[T] = {
      assert(x.currency == y.currency, s"Can not combine two different currencies '${x.currency}' and '${y.currency}'")
      MoneyY(x.amount |+| y.amount, x.currency)
    }
    override def empty: MoneyY[T] = MoneyY[T](0, c)
  }

  implicit def moneyOrder[C <: CY]: Order[MoneyY[C]] = Order.by{_.amount}
  implicit def monoidMoneyCYWithPartialCollector[C <: CY](c: C, unsafeCollector: PartialRateConverter[CY, C]): Monoid[MoneyY[CY]] = new Monoid[MoneyY[CY]] {
    implicit val monoidOfManeyC = fMoneyYMonoid(c)
    override def combine(x: MoneyY[CY], y: MoneyY[CY]): MoneyY[CY] = //FIXME cast
      monoidOfManeyC.combine(unsafeCollector.convert(x), unsafeCollector.convert(y)).asInstanceOf[MoneyY[CY]]

    override def empty: MoneyY[CY] = MoneyY[CY](0, c)
  }
}
