package app.fmgp.money.experimental

import app.fmgp.typeclasses.{_, given _}
import app.fmgp.money.instances.{_, given _}
import app.fmgp.money._
import app.fmgp.money.Currency

//object Instances extends app.fmgp.money.instances.InstancesForCurrency[Currency]

/** test:runMain app.fmgp.money.Experiments */
object Experiments extends App {

  /** This is a subset of Currencies that will be used*/
  //type CURRENCY = GBP.type | USD.type | EUR.type //| JPY.type
  //object Instances extends app.fmgp.money.instances.InstancesForCurrency[Currency]
  //import Instances.moneyTree.{given _, _}

  val a = Money(100, USD)
  val b = Money(200, USD)
  val c = Money(300, GBP)
  val d = Money(9000, EUR)
  val e = Money(1, JPY)

  println(a)
  println(s"Must be 300: ${a + b}")
  //println(s"Must fail  : ${a+c}") //Must fail compilation

  //import scala.language.strictEquality
  //println(Money(0, EUR) == Money(0, GBP)) //Must fail compilation with import scala.language.strictEquality

  // ### MONAD ####
  val m1: MoneyTree[Money[_]] = a.pure :+ b :+ c :+ d
  val m2: MoneyTree[Money[_]] = a.pure.concat(Seq(b, c, d))

  println(m1)
}

