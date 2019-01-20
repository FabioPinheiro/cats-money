package app.fmgp.sandbox

import app.fmgp.money.{CurrencyY, MoneyTree, MoneyY}
import cats.implicits._
import cats.kernel.Monoid

case class Rate[F <: CurrencyY.CY, T <: CurrencyY.CY](from: F, to: T, rate: BigDecimal) {

  def convert(money: MoneyY[F]): MoneyY[T] = MoneyY[T](money.amount * rate, to)

  def convert(wallet: Wallet[CurrencyY.CY]): Wallet[CurrencyY.CY] = {
    implicit val monoidOfT: Monoid[MoneyY[T]] = app.fmgp.money.instances.money.moneyYMonoidT(to)
    val a = wallet.get(from).map(e => MoneyY.fromTuple(e)).map(e => convert(e))
    val b = wallet.get(to).map(e => MoneyY.fromTuple(e))
    (a |+| b).map(e => wallet.update(e.currency, e.amount)).getOrElse(wallet)
  }

  def convert(tree: MoneyTree[MoneyY[CurrencyY.CY]]): MoneyTree[MoneyY[CurrencyY.CY]] = {
    app.fmgp.money.instances.moneyTree.MoneyTreeFunctor.map(tree) {
      case MoneyY(amount, `from`) => MoneyY(amount * rate, to)
      case m => m
    }
  }
}

//TODO, startDate: DateTIME) RANGE

//TODO Rates(rates: Seq[FxRate]) //This can be a Set

object Rate {
  //implicit final def rate1[T <: Currency.CurrencyX](x: T) = Rate(from = x, to = x, rate = BigDecimal(1))
}



