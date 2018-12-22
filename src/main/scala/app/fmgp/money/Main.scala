package app.fmgp.money

//def f[T](t:T)(implicit tag: scala.reflect.ClassTag[T])= tag
//def f[T](t:T)(implicit tag: scala.reflect.runtime.universe.TypeTag[T])= tag

object Main extends App {

  import cats.syntax.monoid._
  import cats.Eq
  import cats.syntax.eq._ // for ===

  import app.fmgp.money._ //TEST CONSOLE
  import app.fmgp.money.MoneyX._
  import app.fmgp.money.MoneyXMonoid._
  import app.fmgp.money.MoneyY._
  import app.fmgp.money.MoneyYMonoid._
  import app.fmgp.money.Wallet._
  import app.fmgp.money.Wallet.X._
  import app.fmgp.money.Wallet.Y._

  import app.fmgp.money.CurrencyY._

  val a: MoneyY[CurrencyY.CurrencyY] = MoneyY(111, USD)
  val b = MoneyY(222, USD)
  val c = MoneyY(9000, EUR)

  println(a, b, c)
  val w1: Wallet[CurrencyY] = Wallet.Y.fromMoney(a) |+| Wallet.Y.fromMoney(b) |+| Wallet.Y.fromMoney(c)
  val w11 = Wallet.Y.fromMoney(b) |+| Wallet.Y.fromMoney(c)
  println(w1, w11)

  val x0 = MoneyY(100, FFF)
  val x1 = MoneyY(1, FFF1)
  val x2 = MoneyY(10, FFF2)
  val rate1 = Rate(FFF,EUR,1.5)
  val w2: Wallet[CurrencyY] = Wallet.Y.fromMoney(x0) |+| Wallet.Y.fromMoney(x1) |+| Wallet.Y.fromMoney(x2) //|+| Wallet.Y.fromMoney(c)
  println(w2, rate1.convert(w2)) //FIXME

  //TODO:
  //- implemente the rates convertion

  //- support minor currencies
}



//import app.fmgp.money.Currency._
//  val a: MoneyX[Currency.Value] = MoneyX(111, USD)
//  val b = MoneyX(222, USD)
//  val c = MoneyX(9000, EUR)
//
//  val r1 = a |+| b |+| c // THIS will return the sum (without account from the currency) FIXME THIS WILL NOTE WORK !
//  val r2: Wallet[CurrencyX] = a.toMoneyX |+| b.toMoneyX |+| c.toMoneyX
//  println(r1)
//  println(r2)
