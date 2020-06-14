package app.fmgp.money.experimental

import app.fmgp.money._
import app.fmgp.money.Currency
import cats.syntax.monoid._
// /import cats.kernel._
import cats._

object Instances extends app.fmgp.money.instances.InstancesForCurrency[Currency]

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

  val monadTree = new app.fmgp.money.instances.MoneyTreeMonad //implicitly[MoneyTree[Money[_]]] // cats syntax is .pure[MoneyTree]
  val m1: MoneyTree[Money[_]] = monadTree.pure(a) :+ b :+ c :+ d
  val m2: MoneyTree[Money[_]] = monadTree.pure(a).concat(Seq(b, c, d))
}
