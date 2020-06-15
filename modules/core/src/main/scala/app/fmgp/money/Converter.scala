package app.fmgp.money


import app.fmgp.typeclasses.{_, given _}

object converter {
  def [in, out](m: Money[in]).inTo(b:out)(using conv: converterBuilder[in, out]) : Money[out] = conv(b)(m)

  type converterFunction[in, out] = Money[in] => Money[out]
  type converterBuilder[in, out] = out => converterFunction[in, out]
  //type getRate[in, out] = in => Rate[in, out]
  type getRate[in, out] = (in, out) => Rate[in, out]

  class MoneyConverter[Ti, To](ti:Ti, rate: RateValue, to:To) extends converterFunction[Ti, To] {
    def apply(in: Money[Ti]) = Money[To](in.amount * rate, to)
  }

  class Converter[Ti, To <: Ti](ti:Ti, to:To)(using rate: getRate[Ti, To]) extends converterFunction[Ti, To] { 
    def apply(in: Money[Ti]) = Money[To](in.amount * rate(in.currency, to).value, to)
  }


  // ### Rates ###
  def [A, B](x: Rate[A, B]).inv: Rate[B,A] = Rate[B,A](1 / x.value)
  def [A, B, C](ab: Rate[A, B]).combine(bc: Rate[B, C]): Rate[A,C] = Rate[A,C](ab.value * bc.value)

  opaque type Rate[-FROM, +TO] = RateValue
  object Rate {
    def apply[FROM, TO](d: RateValue): Rate[FROM, TO] = d
    def getValue[FROM, TO](rate: Rate[FROM, TO]) = rate.value
  }

  extension RateOps on [A,B](x: Rate[A, B]) { 
    def value: RateValue = x
    def toDouble: Double = x.toDouble
  }

}
