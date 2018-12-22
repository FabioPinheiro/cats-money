package app.fmgp.sandbox

import app.fmgp.money.Currency
import cats.Order
import cats.kernel.CommutativeSemigroup
import cats.instances.bigDecimal.catsKernelStdOrderForBigDecimal

case class Money0(amount: BigDecimal, currency: Currency.Value) {
  //def unary_- : Money0 = this.copy(amount = -amount)
  //def +(that: Money0): Money0
  def +(that: Money0): Money0 = {
    require(that.currency == this.currency, s"Different currencies")
    Money0(this.amount + that.amount, this.currency)
  }
  //def -(that: Money0): Money0
  //def * (multiplicator: Int): Money0
  //def * (multiplicator: Float): Money0
  //def * (multiplicator: BigDecimal): Money0
  //def / (divisor: Int): Money0
  //def / (divisor: Float): Money0
  //def / (divisor: BigDecimal)
  //def <(that: Money0): Boolean
  //def >(that: Money0): Boolean
}

object Money0 {
  //FIXME try to make a zero with no currency!!! def zero(currency: Currency.Value): Money0 = Money0(0, currency)
  implicit val MoneyOrder: Order[Money0] = Order.by(_.amount)

  implicit val MoneyCommutativeSemigroup: CommutativeSemigroup[Money0] = new CommutativeSemigroup[Money0] {
    override def combine(x: Money0, y: Money0): Money0 = x + y
  }
}
