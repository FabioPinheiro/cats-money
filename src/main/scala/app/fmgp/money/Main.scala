package app.fmgp.money

import cats.Id
import cats.data.WriterT
import cats.instances.vector._
import cats.syntax.all._

object Main extends App {
  import app.fmgp.money.CurrencyY._

  val a = MoneyY(100, USD)
  val b = MoneyY(200, USD)
  val c = MoneyY(300, GBP)
  val d = MoneyY(9000, EUR)

  import cats.syntax.applicative._
  import app.fmgp.money.instances.CY.all._

  type MonetaryValue = MoneyTree[MoneyY[CurrencyY.CY]]

  val m: MonetaryValue = a.pure[MoneyTree] :+ b :+ c :+ d
  val m2: MonetaryValue = a.pure[MoneyTree].concat(Seq(b, c, d))
  assert(m == m2)
  println(m)
  //MoneyTree(MoneyY(100,USD), MoneyY(200,USD), MoneyY(300,GBP), MoneyY(9000,EUR)})

  val rc = PartialRateConverter[CY, EUR.type](EUR, Map(USD -> 0.8, GBP -> 1.1))
  implicit val shouldWeAddTheMonoidInsideTheRc = moneyYMonoidT(EUR)

  val ret = m.convertWithLog(rc)
  assert(ret.total.value == m.convert(rc).total)
  println(s"The total after convert is ${ret.total.value}}")
  //The total after convert is MoneyY(9570.0,EUR)}

  def report[T](x: WriterT[Id, Vector[String], MoneyY[T]]) = {
    val (log, total) = x.run
    log.mkString("### Report ###\n", "\n", s"\nConvert with log then total: $total\n##############")
  }

  println(report(ret.total))
  //### Report ###
  //100USD(0.8)->80.0EUR
  //200USD(0.8)->160.0EUR
  //300GBP(1.1)->330.0EUR
  //Convert with log then total: MoneyY(9570.0,EUR)
  //##############

  val commission = for {
    _ <- Vector("Add commissions").tell
    fabio <- MoneyY(99.99, EUR).writer(Vector("Fabio's commission is 99.99")) //.pure[Logged]
    joao <- MoneyY(20, EUR).writer(Vector("Joao's commission is 20")) //.pure[Logged]
  } yield fabio |+| joao

  val ret2 = ret :+ commission
  println(report(ret2.total))
  println(ret2.total.value)
  //### Report ###
  //100USD(0.8)->80.0EUR
  //200USD(0.8)->160.0EUR
  //300GBP(1.1)->330.0EUR
  //Add commissions
  //Fabio's commission is 99.99
  //Joao's commission is 20
  //Convert with log then total: MoneyY(9689.99,EUR)
  //##############
  //MoneyY(9689.99,EUR)
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
