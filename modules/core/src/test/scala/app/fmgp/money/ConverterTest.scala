package app.fmgp.money

import app.fmgp.money.converter._
import app.fmgp.money.converter.Rate._
import app.fmgp.money.ConverterTest.{_, given _}


object ConverterTest {

  type TestSet = GBP.type | EUR.type | USD.type

  //summon[EUR.type => Rate[EUR.type,USD.type]]
  def rates2USD[A]: getRate[A, USD.type] =
    (a: A, b: USD.type) =>
      a match {
        case EUR => Rate(1.12d)
        case USD => Rate(1d)
        case GBP => Rate(1.25d)
      }

  //given rateA2B[A <: Currency, B <: Currency] as getRate[A, B] {
  given rateA2B [A, B] as getRate [A, B] {
    def apply(a: A, b: B) = rates2USD(a, USD) combine rates2USD(b, USD).inv
  }

  given converterBuilderTestSet [Ti, To] (using rate: getRate[Ti, To]) as converterBuilder[Ti, To] {
    def apply(to: To) = (in: Money[Ti]) => Money[To](in.amount * rate(in.currency, to).value, to)
  }
}

class ConverterTest extends munit.FunSuite {
  import app.fmgp.money.converter._

  test("ConverterTest with extension method .inTo") {
    assert(Money(100, EUR).inTo(USD) == Money(112, USD))
    assert(Money(100, USD).inTo(GBP) == Money(80, GBP))
    assert(Money(100, EUR).inTo(GBP) == Money(89.600, GBP))
  }
}
