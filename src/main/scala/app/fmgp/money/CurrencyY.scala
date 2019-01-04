package app.fmgp.money

import cats.kernel.Eq

object CurrencyY {
  implicit val eqv: Eq[CY] = Eq.fromUniversalEquals

  abstract sealed class CY(name: String)

  object XCY {
    def apply(name: String = "XXX"): CY = new CY(name) {}
    def empty = apply()
  }

  case object XXX extends CY("XXX") //Denote a "transaction" involving no currency.
  case object XTS extends CY("XTS") //Reserved for use in testing.
  //case object XBT extends CY("XBT") //Cryptocurrency Bitcoin but is not ISO 4217 approved
  case object XAU extends CY("XAU") //GOLD
  case object USD extends CY("USD") //
  case object GBP extends CY("GBP") //
  case object EUR extends CY("EUR") //

  //TODO We can use shapeless to do this ... (But I need to learn it fist! XD)
  implicit def companionXXX = new Companion[XXX.type] {type C = XXX.type; def apply() = XXX}
  implicit def companionXTS = new Companion[XTS.type] {type C = XTS.type; def apply() = XTS}
  implicit def companionUSD = new Companion[USD.type] {type C = USD.type; def apply() = USD}
  implicit def companionXAU = new Companion[XAU.type] {type C = XAU.type; def apply() = XAU}
  implicit def companionGBP = new Companion[GBP.type] {type C = GBP.type; def apply() = GBP}
  implicit def companionEUR = new Companion[EUR.type] {type C = EUR.type; def apply() = EUR}
}

//TODO ... ISO-4217
trait Currency {
  abstract sealed class CCC(name: String)
  case object AED extends CCC("AED")
  case object AUD extends CCC("AUD")
  case object BWP extends CCC("BWP")
  case object CAD extends CCC("CAD")
  case object CHF extends CCC("CHF")
  case object EUR extends CCC("EUR")
  case object GBP extends CCC("GBP")
  case object JPY extends CCC("JPY") //was not minor type *1
  case object LYD extends CCC("LYD") //Minor type *1000
  case object MAD extends CCC("MAD")
  case object MUR extends CCC("MUR")
  case object NAD extends CCC("NAD")
  case object NZD extends CCC("NZD")
  case object OMR extends CCC("OMR")
  case object THB extends CCC("THB")
  case object USD extends CCC("USD")
  case object ZAR extends CCC("ZAR")
  case object FJD extends CCC("FJD")
}


object EUR_XXX {
  import shapeless.{:+:, CNil}
  type CurencyList = CurrencyY.EUR.type :+: CurrencyY.XXX.type :+: CNil
  import shapeless.union.Union
  type Curency = Union.`'eur -> CurrencyY.EUR, 'usd -> CurrencyY.USD`.T
}

object Currency extends Currency {

  import shapeless._, record._, union._, syntax.singleton._

  val xpto = Generic[CCC]
  type AUX = EUR.type :+: GBP.type :+: USD.type :+: CNil

  def createCompanion[A](func: A => A): Companion[A] =
    new Companion[A] {
      override type C = A
      override def apply(): A = func(GBP.asInstanceOf[A]) //FIXME
    }

  implicit def companion = new Companion[USD.type] {type C = USD.type; def apply() = USD}


  implicit val genericCompanionCNil: Companion[CNil] = {
    new Companion[CNil] {
      override type C = Nil.type
      override def apply(): C = Nil
    }
    //throw new Exception("Inconceivable!")
  }
  implicit val genericCompanionCList: Companion[CNil] = {
    new Companion[CNil] {
      override type C = Nil.type
      override def apply(): C = Nil
    }
    // throw new Exception("Inconceivable!")
  }
//
//  implicit def coproductCompanion[H, T <: Coproduct](
//    implicit
//    hCompanion: Companion[H],
//    tCompanion: Companion[T]
//  ): Companion[H :+: T] = createCompanion {
//    case Inl(h) => hCompanion()
//    case Inr(t) => tCompanion(t)
//  }

  def test = {
    print("Currency TEST")
    val a = xpto.to(USD)
    val b = xpto.to(AED)
    println(a)
    println(b)
    //val c = xpto.from(CNil)
    //println(c)
  }
}
