package app.fmgp.money

import cats.syntax.all._
//import cats.implicits._

object Main extends App {

  import app.fmgp.money.CurrencyY._
  import app.fmgp.money.instances.CY.all._

  val aa = MoneyZ[USD.type](100)
  val bb = MoneyZ[USD.type](200)
  val cc = MoneyZ[GBP.type](300)
  val dd = MoneyZ[EUR.type](9000)

  import app.fmgp.money.Companion._
  //import app.fmgp.money.Companion.MoneyWithCompanion //to get currency

  println(s"MoneyZ: .currency  ${aa.currency}")
  println(s"MoneyZ: Companion  ${Companion.of(aa)}")
  println(s"MoneyZ: <+>        ${aa <+> bb}")
  println(s"MoneyZ: show       ${show"$aa"}")

  Currency.test
}

/*
import app.fmgp.sandbox.{Rate, Wallet}
import cats.syntax.monoid._
import cats.Eq
import cats.syntax.eq._ // for ===
import app.fmgp.sandbox.Wallet.Y._
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


//import app.fmgp.money.Currency._
//  val a: MoneyX[Currency.Value] = MoneyX(111, USD)
//  val b = MoneyX(222, USD)
//  val c = MoneyX(9000, EUR)
//
//  val r1 = a |+| b |+| c // THIS will return the sum (without account from the currency) FIXME THIS WILL NOTE WORK !
//  val r2: Wallet[CurrencyX] = a.toMoneyX |+| b.toMoneyX |+| c.toMoneyX
//  println(r1)
//  println(r2)


//  import scala.reflect.runtime.universe
//  import scala.reflect.runtime.universe._
//  implicit class TypeDetector[T: TypeTag](related: MoneyZ[T]) {
//    def getTag: universe.TypeTag[T] = typeTag[T]
//    def getType: Type = typeOf[T]
//    def getSymbol: universe.TypeSymbol = symbolOf[T]
//  }
// println("MoneyZ", aa.getTag, aa.getType, aa.getSymbol.toTypeConstructor,
