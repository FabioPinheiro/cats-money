package app.fmgp.money

import cats.kernel.{Eq, Monoid, Order}

sealed abstract case class MoneyY[T <: CurrencyY.CY](amount: BigDecimal, currency: T)

object MoneyY {
  def apply[T <: CurrencyY.CY](amount: BigDecimal, t: T): MoneyY[T] = new MoneyY[T](amount, t) {} //TODO need to round
  def fromTuple[T <: CurrencyY.CY](m: (T, BigDecimal)): MoneyY[T] = MoneyY.apply(m._2, m._1)
  //def zero[T <: CurrencyY.CY](t: T): MoneyY[T] = new MoneyY[T](BigDecimal(0), t) {}
  implicit def eqv[T <: CurrencyY.CY]: Eq[MoneyY[T]] = Eq.fromUniversalEquals
}

object MoneyYMonoid {

  import cats.syntax.monoid._
  import cats.instances.bigDecimal._

  implicit val MoneyOrder: Order[MoneyY[_]] = Order.by(_.amount)

  import scala.language.implicitConversions

  implicit def fMoneyYMonoid[C <: CurrencyY.CY](c: C): Monoid[MoneyY[C]] = new Monoid[MoneyY[C]] {
    override def combine(x: MoneyY[C], y: MoneyY[C]): MoneyY[C] = MoneyY(x.amount |+| y.amount, x.currency)
    override def empty: MoneyY[C] = MoneyY[C](0, c)
  }
}
