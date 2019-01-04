package app.fmgp.money

import cats.Id
import cats.data.WriterT
import cats.instances.vector._
import cats.kernel.Monoid
import cats.syntax.all._
//import cats.syntax.applicative._

object Demo extends App {

  import app.fmgp.money.CurrencyY._

  val a = MoneyY(100, USD)
  val b = MoneyY(200, USD)
  val c = MoneyY(300, GBP)
  val d = MoneyY(9000, EUR)

  import app.fmgp.money.instances.CY.all._

  type MonetaryValue = MoneyTree[MoneyY[CurrencyY.CY]]

  val m: MonetaryValue = a.pure[MoneyTree] :+ b :+ c :+ d
  val m2: MonetaryValue = a.pure[MoneyTree].concat(Seq(b, c, d))
  assert(m == m2)
  println(m) //MoneyTree(MoneyY(100,USD), MoneyY(200,USD), MoneyY(300,GBP), MoneyY(9000,EUR)})

  val rc = PartialRateConverter[CY, EUR.type](EUR, Map(USD -> 0.8, GBP -> 1.1))
  implicit val shouldWeAddTheMonoidInsideTheRc: Monoid[MoneyY[CurrencyY.EUR.type]] = moneyYMonoidT(EUR)

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
