package app.fmgp.money

import app.fmgp.money.instances.MoneyInstances.{MoneyZWithTag, ring}
import cats.syntax.all._

object Main extends App {

  import app.fmgp.money.CurrencyY._
  import app.fmgp.money.instances.all._

  val aa = MoneyZ[USD.type](100)
  val bb = MoneyZ[USD.type](200)
  val cc = MoneyZ[GBP.type](300)
  val dd = MoneyZ[EUR.type](9000)

  println(s"MoneyZ: .currency  ${aa.currency}")
  println(s"MoneyZ: Companion  ${Companion.of(aa)}")
  println(s"MoneyZ: <+>        ${aa <+> bb}")
  println(s"MoneyZ: show       ${show"$aa"}")

  Currency.test

  import shapeless._, record._, union._, syntax.singleton._

//  implicit val gUSD = Generic[MoneyZ[USD.type]]
//  implicit val gEUR = Generic[MoneyZ[EUR.type]]
//  implicit val gGBP = Generic[MoneyZ[GBP.type]]
//  //def gXPTO[T] = Generic[MoneyZ[T]]
//
//  val f = (gEUR.from _).compose(gGBP.to _)
//  println(f, f.apply(cc).show)

  implicit class MoneyXPTO[FROM](value: MoneyZ[FROM]) {
    def to[TO, R <: HList](c: TO)(
      implicit
      genFrom: Generic.Aux[MoneyZ[FROM], R],
      genTo: Generic.Aux[MoneyZ[TO], R],
      fromRing: MoneyZWithTag[FROM],
      toRing: MoneyZWithTag[TO]
    ): MoneyZ[TO] = {
      object ring extends Poly3 {
        val fa: MoneyZ[FROM] => R = genFrom.to _
        val fb: R => MoneyZ[TO] = genTo.from _
        val fab: MoneyZ[FROM] => MoneyZ[TO] = fa.andThen(fb)

        implicit val r2rCase: Case.Aux[MoneyZ[FROM], MoneyZWithTag[FROM], MoneyZWithTag[TO], MoneyZ[TO]] = at { (v, aa, bb) =>
          val inputValueOnBase = v.asInstanceOf[MoneyZWithTag[FROM]] <+> aa
          val outputValueOnBase = fab.apply(inputValueOnBase)
          outputValueOnBase.asInstanceOf[MoneyZWithTag[TO]] <+> bb
        }
      }
      ring(value, fromRing, toRing)
    }
  }

  implicit val xGDP = ring(GBP, 1.2) //same as  MoneyZ[GBP.type](1.2d).asInstanceOf[MoneyZWithTag[CurrencyY.GBP.type]]
  implicit val xUSD = ring(USD, 1) //same as MoneyZ[USD.type](1d).asInstanceOf[MoneyZWithTag[CurrencyY.USD.type]]
  implicit val xEUR = ring(EUR, 1.32)

  println(cc.show, cc.to(USD).show, cc.to(USD).to(USD).show)
  println(cc.show, cc.to(EUR).show, cc.to(USD).to(EUR).show)


  //val aux: Vector[MoneyZWithTag[_]] = runtime.toVector.map(e => ring(e._1, e._2)).map(e => println(moneyZShow.show(e))) //This don't not work type erasure
  val runtime = Map(GBP->1.2d, USD -> 1d, EUR -> 1.32d)
  val ringHLint = (GBP->1.2d) :: (USD -> 1d) :: (EUR -> 1.32d) :: HNil
  val head = ringHLint.head
  println(ringHLint, head)
  //TODO convert a Seq into a ring at runtime
  println(ring(head._1,head._2).show)
  val base: HList = EUR :: HNil
  def loop2 = USD :: base
  def loop3 = GBP :: loop2
  println(loop3)
  println(ringHLint.unzip._1)

  def OP(vector:Vector[_]): HList = {
    def loop(v:Seq[_], l:HList): HList = v match {
      case x if !x.isEmpty => loop(x.tail, x.head :: l)
      case _ => l
    }
    loop(vector, HNil)
  }
  val types = Vector(EUR, USD, GBP)
  println(OP(types))

  import scala.reflect.runtime.universe._
  println(reify(ring(GBP, 123)))

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
