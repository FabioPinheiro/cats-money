package app.fmgp.money


case class Rate[F <: CurrencyY.CurrencyY, T <: CurrencyY.CurrencyY](from: F, to: T, rate: BigDecimal) {

  def convert(money: MoneyY[F]): MoneyY[T] = MoneyY[T](money.amount * rate, to)

  def convert2(money: MoneyY[F]): MoneyY[T] = MoneyY[T](money.amount * rate, to)

  def convert(wallet: Wallet[CurrencyY.CurrencyY]): Wallet[CurrencyY.CurrencyY] = {
    import cats.implicits._
    import cats.kernel.Monoid
    implicit val monoidOfT: Monoid[MoneyY[T]] = app.fmgp.money.MoneyYMonoid.fMoneyYMonoid(to)
    val a = wallet.get(from).map(e => MoneyY.fromTuple(e)).map(e => convert(e))
    val b = wallet.get(to).map(e => MoneyY.fromTuple(e))
    (a |+| b).map(e => wallet.update(e.currency, e.amount)).getOrElse(wallet)
  }

  def convert(tree: MoneyTree[MoneyY[CurrencyY.CurrencyY]]): MoneyTree[MoneyY[CurrencyY.CurrencyY]] = {
    import app.fmgp.money.MoneyTree.treeFunctor
    val FROM: F = from
    treeFunctor.map(tree) {
      case m@MoneyY(amount, FROM) => MoneyY(amount * rate, to)
      case m => m
    }
  }
}

//TODO, startDate: DateTIME) RANGE

//TODO Rates(rates: Seq[FxRate]) //This can be a Set

object Rate {
  //implicit final def rate1[T <: Currency.CurrencyX](x: T) = Rate(from = x, to = x, rate = BigDecimal(1))
}



