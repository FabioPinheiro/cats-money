package app.fmgp.money

import app.fmgp.typeclasses.{_, given _}
import app.fmgp.money.converter.{_, given _}
import app.fmgp.money.instances.{_, given _}


/** core/test:runMain app.fmgp.money.Demo */
object Demo extends App {

  /** This is a subset of Currencies that will be used*/
  //type CURRENCY = GBP.type | USD.type | EUR.type
  //| Currency.JPY.type
  // object Instances extends app.fmgp.money.instances.InstancesForCurrency[CURRENCY]
  // import Instances.all.{given _, _}

  val a = Money(100, USD)
  val b = Money(200, USD)
  val c = Money(300, GBP)
  val d = Money(9000, EUR)
  val e = Money(1, JPY)

  val eur2jpy = Converter(EUR, JPY)(using (EUR, JPY) => Rate(119.78))
  println(eur2jpy(Money(1, EUR))) //Money(119.78,app.fmgp.money.JPY)

  import ConverterTest.{given _}
  println(Money(100, EUR).inTo(GBP)) //Money(89.600,app.fmgp.money.GBP)

  val m1 = a.pure :+ b :+ c :+ d
  val m2 = a.pure.concat(Seq(b, c, d))
  assert(m1 == m2)
  println(m1) //MoneyTree(Money(100,USD), Money(200,USD), Money(300,GBP), Money(9000,EUR)})
  println(m1.collapse)

  // // #####################
  // // ### RateConverter ###
  // // #####################
  // val rc = PartialRateConverter[CURRENCY, EUR.type](EUR, Map[CURRENCY, BigDecimal](USD -> 0.8, GBP -> 1.1))
  // //implicit val shouldWeAddTheMonoidInsideTheRc: Monoid[Money[EUR.type]] = MoneyMonoidT(EUR)
  // given shouldWeAddTheMonoidInsideTheRc as Monoid[Money[EUR.type]] = MoneyMonoidT(EUR)
  // // ####################
  // // ### Traceability ###
  // // ####################
  // //TODO MoneyTree With Traceability
  // val ret = m.convertWithLog(rc)
  // assert(ret.total.value == m.convert(rc).total)
  // println(s"The total after convert is ${ret.total.value}") //The total after convert is Money(9570.0,EUR)}

  // def report[T](x: WriterT[Id, Vector[String], Money[T]]) = {
  //   val (log, total) = x.run
  //   log.mkString("### Report ###\n", "\n", s"\nConvert with log then total: $total\n##############")
  // }

  // println(report(ret.total))
  // //### Report ###
  // //100USD(0.8)->80.0EUR
  // //200USD(0.8)->160.0EUR
  // //300GBP(1.1)->330.0EUR
  // //Convert with log then total: Money(9570.0,EUR)
  // //##############
  // //import app.fmgp.money.instances.all._
  // //import app.fmgp.money.instances.money._
  // //import app.fmgp.money.instances.moneyTree._
  // import cats.catsInstancesForId
  // val commission = for {
  //   _ <- Vector("Add commissions").tell
  //   fabio <- Money[EUR.type](99.99, EUR).writer(Vector("Fabio's commission is 99.99")) //.pure[Logged]
  //   //home <- WriterT.value[Id, Vector[String], Money[EUR.type]](Money[EUR.type](10000000, EUR)) //Money[EUR.type](20, EUR).value
  //   joao <- Money[EUR.type](20, EUR).writer(Vector("Joao's commission is 20")) //.pure[Logged]
  // } yield (fabio |+| joao )

  // println("")
  // val ret2 = ret :+ commission
  // println(report(ret2.total))
  // //### Report ###
  // //100USD(0.8)->80.0EUR
  // //200USD(0.8)->160.0EUR
  // //300GBP(1.1)->330.0EUR
  // //Add commissions
  // //Fabio's commission is 99.99
  // //Joao's commission is 20
  // //Convert with log then total: Money(9689.99,EUR)
  // //##############
  // println(ret2.total.value)
  // //Money(9689.99,EUR)
  // // Done try play with derivation and Multiversal Equality
  // // https://dotty.epfl.ch/docs/reference/contextual/derivation.html
  // // https://dotty.epfl.ch/docs/reference/contextual/multiversal-equality.html

  // //println(USD == GBP) //Done how do I make this not to compile
  // //println(USD.eqv(GBP)) // Not implemented
  // println(a == Money[USD.type](100, USD)) //ok (true)
  // println(a.eqv(Money[USD.type](100, USD))) //ok (true)
  // println(a == b) //ok (false)
  // println(a.eqv(b)) //ok (false)
  // //println(c == a) //Done how do I make this not to compile
  // //println(a.eqv(c)) //ok (Compilation failed)

  // //same Box[Currency.EUR.type](Currency.EUR) == Box[Currency.USD.type](Currency.USD)
}
