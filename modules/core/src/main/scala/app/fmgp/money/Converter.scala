package app.fmgp.money

object converter {
  extension [I,O](m: Money[I])
    def inTo(b:O)(using conv: converterBuilder[I, O]) : Money[O] = conv(b)(m)

  type converterFunction[in, out] = Money[in] => Money[out]
  type converterBuilder[in, out] = out => converterFunction[in, out]
  type getRate[in, out] = (in, out) => Rate[in, out]

  class MoneyConverter[Ti, To](ti:Ti, rate: RateValue, to:To) extends converterFunction[Ti, To] {
    def apply(in: Money[Ti]) = Money[To](in.amount * rate, to)
  }

  class Converter[Ti, To <: Ti](ti:Ti, to:To)(using rate: getRate[Ti, To]) extends converterFunction[Ti, To] {
    def apply(in: Money[Ti]) = Money[To](in.amount * rate(in.currency, to), to)
  }


  // ### Rates ###
  extension [A, B](x: Rate[A, B])
    def inv: Rate[B,A] = Rate[B,A](1 / x)

  extension [A, B, C](ab: Rate[A, B])
    def combine(bc: Rate[B, C]): Rate[A,C] = Rate[A,C](ab * bc)

  opaque type Rate[-FROM, +TO] = RateValue
  object Rate {
    def apply[FROM, TO](d: RateValue): Rate[FROM, TO] = d
    def getValue[FROM, TO](rate: Rate[FROM, TO]) = rate
  }

  extension [A,B](x: Rate[A, B]) {
    def value: RateValue = x
    def toDouble: Double = x.toDouble
  }

}
