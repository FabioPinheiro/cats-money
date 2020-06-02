package app.fmgp.money.experimental

import app.fmgp.money._
import app.fmgp.money.Currency._
//import scala.language.strictEquality


/** test:runMain app.fmgp.money.Experiments */
object Experiments extends App {

  type CURRENCY = Currency.GBP.type | Currency.USD.type | Currency.EUR.type //| Currency.JPY.type
  //type CURRENCY = Currency.type
  object Instances extends app.fmgp.money.instances.InstancesForCurrency[CURRENCY]
  import Instances.all.{given _, _}
  
  val a = Money(100, USD)
  val b = Money(200, USD)
  val c = Money(300, GBP)
  val d = Money(9000, EUR)
  val e = Money(1, JPY)

  println(a)
  println(s"Must be 300: ${a+b}")
  //println(s"Must fail  : ${a+c}") //Must fail compilation
}
