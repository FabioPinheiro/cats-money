package app.fmgp.money

import app.fmgp
import app.fmgp.money
import app.fmgp.sandbox.{Rate, Wallet}

//def f[T](t:T)(implicit tag: scala.reflect.ClassTag[T])= tag
//def f[T](t:T)(implicit tag: scala.reflect.runtime.universe.TypeTag[T])= tag

object Main extends App {

  import cats.syntax.monoid._
  import cats.Eq
  import cats.syntax.eq._ // for ===

  import app.fmgp.money._ //TEST CONSOLE
  import app.fmgp.sandbox.MoneyX._
  import app.fmgp.sandbox.MoneyXMonoid._
  import app.fmgp.money.MoneyY._
  import app.fmgp.money.MoneyYMonoid._
  import app.fmgp.sandbox.Wallet._
  import app.fmgp.sandbox.Wallet.X._
  import app.fmgp.sandbox.Wallet.Y._

  import app.fmgp.money.CurrencyY._

  val a: MoneyY[CurrencyY.CY] = MoneyY(111, USD)
  val b = MoneyY(222, USD)
  val c = MoneyY(9000, EUR)

  println(a, b, c)
  val w1: Wallet[CY] = Wallet.Y.fromMoney(a) |+| Wallet.Y.fromMoney(b) |+| Wallet.Y.fromMoney(c)
  val w11 = Wallet.Y.fromMoney(b) |+| Wallet.Y.fromMoney(c)
  println(w1, w11)

  //  val x0 = MoneyY(100, FFF)
  //  val x1 = MoneyY(1, FFF1)
  //  val x2 = MoneyY(10, FFF2)
  //  val rate1 = Rate(FFF, EUR, 1.5)
  //  val w2: Wallet[CurrencyY] = Wallet.Y.fromMoney(x0) |+| Wallet.Y.fromMoney(x1) |+| Wallet.Y.fromMoney(x2) //|+| Wallet.Y.fromMoney(c)
  //  println(w2, rate1.convert(w2)) //FIXME


  import shapeless._, record._, union._, syntax.singleton._

  type U = Union.`'a -> EUR, 'b -> USD`.T
  val u1 = Coproduct[U]('a ->> EUR)
  println("u1", u1)





  import shapeless._, record._, union._, syntax.singleton._

  object polymorphicF extends Poly1 {
    implicit def caseEUR = at[EUR.type](s => 1.4)
    implicit def caseUSD = at[USD.type](s => "asd")
    implicit def caseGBP = at[GBP.type](s => 1.1)
    implicit def caseaGBP = at[CY](s => 0)
  }
  type CU = EUR.type :+: USD.type :+: CNil
  val cu = Coproduct[CU](USD)

  //val bbb: Aux[money.CurrencyY.CY, HNil] = Generic[CY]
  val aa: Coproduct = cu.map(polymorphicF)
  val to: CY = EUR
  val cc = polymorphicF(to)
  println("cu", cu, cu.map(polymorphicF), polymorphicF(EUR), cc)
  //println(CU)

  object size extends Poly1 {
    implicit def caseInt = at[Int](x => 1)
    implicit def caseString = at[String](_.length)
    implicit def caseTuple[T, U]
    (implicit st : Case.Aux[T, Int], su : Case.Aux[U, Int]) =
      at[(T, U)](t => size(t._1)+size(t._2))
  }
  println("size(23)",size(23))

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
