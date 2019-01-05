package app.fmgp.money

import app.fmgp.money.instances.MoneyInstances.MoneyZWithTag
import cats.MonoidK
import cats.syntax.all._
import shapeless.Generic.Aux
//import cats.implicits._

trait MyTag

object Main extends App {

  import app.fmgp.money.CurrencyY._
  //import app.fmgp.money.instances.CY.all._
  import app.fmgp.money.instances.all._

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


  import shapeless._, record._, union._, syntax.singleton._

  //implicit val gUSD = Generic[MoneyZ[USD.type]]
  //implicit val gEUR = Generic[MoneyZ[EUR.type]]
  //implicit val gGBP = Generic[MoneyZ[GBP.type]]
  //def gXPTO[T] = Generic[MoneyZ[T]]

  //val f = (gEUR.from _).compose(gGBP.to _)
  //println(f, f.apply(cc).show)

  implicit class MoneyXPTO[FROM](value: MoneyZ[FROM]) {
    def to[TO, R <: HList](c: TO)(
      implicit
      genFrom: Generic.Aux[MoneyZ[FROM], R],
      genTo: Generic.Aux[MoneyZ[TO], R],
      a: MoneyZ[FROM] with MyTag, b: MoneyZ[TO] with MyTag
    ): MoneyZ[TO] = {
      object xpto extends Poly3 {
        import app.fmgp.money.instances.MoneyMonoidKWithTag
        implicit val xxxx: MonoidK[MoneyZWithTag] = new MoneyMonoidKWithTag
        val fa: MoneyZ[FROM] => R = genFrom.to _
        val fb: R => MoneyZ[TO] = genTo.from _
        val fab: MoneyZ[FROM] => MoneyZ[TO] = fa.andThen(fb)
        val aux: MoneyZ[FROM] with MyTag = value.asInstanceOf[MoneyZWithTag[FROM]]
        val zz: (MoneyZ[MoneyMonoidKWithTag], MoneyZ[MoneyMonoidKWithTag]) => MoneyZ[MoneyMonoidKWithTag] = xxxx.combine _
        implicit val r2rCase: Case.Aux[MoneyZ[FROM], MoneyZ[FROM] with MyTag, MoneyZ[TO] with MyTag, MoneyZ[TO]] =
          at((v, aa, bb) => fab.apply(v <+> aa) <+> bb)
      }
      xpto(value, a, b)
    }
  }

  object multiply extends Poly2 {
    implicit val intIntCase: Case.Aux[Double, Double, Double] =
      at((a, b) => a * b)
  }


  //  implicit class MoneyXPTO[FROM](value: MoneyZ[FROM]) {
  //    def to[TO](c: TO)(implicit from: Generic[MoneyZ[FROM]], to: Generic[MoneyZ[TO]]) = {
  //      val fa: MoneyZ[FROM] => from.Repr = from.to _
  //      val fb: to.Repr => MoneyZ[TO] = to.from _
  //      val f: MoneyZ[FROM] => MoneyZ[TO] = fa.compose(fb)
  //      f.apply(value)
  //    }
  //  }
  implicit val a = MoneyZ[GBP.type](1.2d).asInstanceOf[MoneyZ[CurrencyY.GBP.type] with MyTag]
  implicit val b = MoneyZ[USD.type](1d).asInstanceOf[MoneyZ[CurrencyY.USD.type] with MyTag]
  println(cc.to(USD))
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
