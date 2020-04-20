package app.fmgp.money

//import cats.kernel.Eq
//import shapeless._, record._, union._, syntax.singleton._

//TODO ... ISO-4217
trait Currency {
  enum CCC { //abstract sealed class CCC(name: String)
  //case AED 
  //case AUD 
  //case BWP 
  //case CAD 
  //case CHF 
  
  case EUR 
  case GBP 
 
  // case JPY  //was not minor type *1
  // case LYD  //Minor type *1000
  // case MAD 
  // case MUR 
  // case NAD 
  // case NZD 
  // case OMR 
  // case THB 
  case USD 
  // case ZAR 
  // case FJD 
  }
}

object Currency extends Currency {

  type AUX = CCC.EUR.type | CCC.GBP.type | CCC.USD.type


  def createCompanion[A](func: A => A): Companion[A] =
    new Companion[A] {
      override type C = A
      override def apply(): A = func(CCC.GBP.asInstanceOf[A]) //FIXME
    }

  given companion as Companion [CCC.USD.type] {
    type C = CCC.USD.type
    def apply() = CCC.USD
  }
}
