package app.fmgp.money

import app.fmgp.money.Currency.CurrencyX
import cats.implicits._
import cats.kernel.{CommutativeGroup, Eq, Monoid}


sealed abstract case class MoneyX[T <: Enumeration#Value](amount: BigDecimal) {
  def currency: T
  def currencyName = currency.toString
  def toWallet: Wallet[T] = Wallet.X.fromMoney[T](this) //FUCK YEAH !! +T
}

object MoneyX {
  def apply[T <: CurrencyX](amount: BigDecimal, t: T): MoneyX[T] = new MoneyX[T](amount) { //TODO need to round
    override def currency: T = t
  }
  def zero[T <: CurrencyX](t: T): MoneyX[T] = new MoneyX[T](BigDecimal(0)) {
    override def currency: T = t
  }
  implicit def eqv[T <: Enumeration#Value]: Eq[MoneyX[T]] = Eq.fromUniversalEquals
}

object MoneyXMonoid {
  //  def fMoneyXMonoid[C <: Enumeration#Value]: Monoid[MoneyX[C]] = new CommutativeGroup[MoneyX[C]] {
  //    override def combine(x: MoneyX[C], y: MoneyX[C]): MoneyX[C] = MoneyX(x.amount |+| y.amount, x.currency)
  //    override def empty: MoneyX[C] = MoneyX.zero(Currency.XXX)
  //  }
  implicit val fMoneyXMonoid: Monoid[MoneyX[CurrencyX]] = new Monoid[MoneyX[CurrencyX]] { //THIS CAN NOT ME A MONOID!
    override def combine(x: MoneyX[CurrencyX], y: MoneyX[CurrencyX]): MoneyX[CurrencyX] = MoneyX(x.amount |+| y.amount, x.currency) //FIXME x.currency
    override def empty: MoneyX[CurrencyX] = MoneyX.zero(Currency.XXX)
  }
}
