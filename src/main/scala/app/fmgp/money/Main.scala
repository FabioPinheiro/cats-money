package app.fmgp.money

import app.fmgp
import app.fmgp.money._
import app.fmgp.sandbox.{Rate, Wallet}
import cats.syntax.monoid._
import cats.Eq
import cats.syntax.eq._ // for ===

//def f[T](t:T)(implicit tag: scala.reflect.ClassTag[T])= tag
//def f[T](t:T)(implicit tag: scala.reflect.runtime.universe.TypeTag[T])= tag

object Main extends App {

  import app.fmgp.sandbox.Wallet.Y._
  import app.fmgp.money.CurrencyY._

  type MonetaryValue = MoneyY[CurrencyY.CY]
  type XPTO = MoneyTree[MonetaryValue]

  val a = MoneyY(100, USD)
  val b = MoneyY(200, USD)
  val c = MoneyY(300, GBP)
  val d = MoneyY(9000, EUR)

  import cats.syntax.applicative._
  import app.fmgp.money.instances.CY.all._

  // for pure
  val m: MoneyTree[MoneyY[CY]] = a.pure[MoneyTree] :+ b :+ c :+ d
  val m2: MoneyTree[MoneyY[CY]] = a.pure[MoneyTree].concat(Seq(b, c, d))
  println(m, m == m2)
  val rc = PartialRateConverter.fromMapRates[CY, EUR.type](EUR, Map(USD -> 0.8, GBP -> 1.1))
  val total = MoneyTree.total(m, rc)(moneyYMonoidT(EUR))

  println(s"TOTAL m is $total")

  /*
  val w1: Wallet[CY] = Wallet.Y.fromMoney(a) |+| Wallet.Y.fromMoney(b) |+| Wallet.Y.fromMoney(c)
  val w11 = Wallet.Y.fromMoney(b) |+| Wallet.Y.fromMoney(c)
  println(w1, w11)

  import shapeless._, record._, union._, syntax.singleton._

  val UUU = Coproduct[EUR_XXX.Curency]('eur ->> EUR)
  val converter = ShapelessConverter(1.1,0)
  println("u1", UUU, converter.poly(XXX))

  val LLL = Coproduct[EUR_XXX.CurencyList](XXX)
  println("cu", LLL, LLL.map(converter.poly), converter.poly(EUR))

  object size extends Poly1 {
    implicit def caseInt = at[Int](x => 1)
    implicit def caseString = at[String](_.length)
    implicit def caseTuple[T, U](implicit st: Case.Aux[T, Int], su: Case.Aux[U, Int]) = at[(T, U)](t => size(t._1) + size(t._2))
  }
  println("size(23)", size((230, 123)))
*/
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
