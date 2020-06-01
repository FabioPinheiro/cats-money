package app.fmgp.money

import app.fmgp.money._
import app.fmgp.money.Currency._

class MoneyTest extends munit.FunSuite {
  test("Money comparison with different currency shound not compile") {
    // Set(2, 1).sorted
    assertNoDiff(
      compileErrors("Money(0, EUR) == Money(0, GBP)"),
      """|error:
         |Values of types app.fmgp.money.Money[app.fmgp.money.Currency.EUR.type] and app.fmgp.money.Money[app.fmgp.money.Currency.GBP.type] cannot be compared with == or !=.
         |I found:
         |
         |    app.fmgp.money.Money.derived$Eql[app.fmgp.money.Currency.EUR.type, 
         |      app.fmgp.money.Currency.GBP.type
         |    ](
         |      /* missing */implicitly[
         |        Eql[app.fmgp.money.Currency.EUR.type, app.fmgp.money.Currency.GBP.type]
         |      ]
         |    )
         |
         |But no implicit values were found that match type Eql[app.fmgp.money.Currency.EUR.type, app.fmgp.money.Currency.GBP.type].
         |Money(0, EUR) == Money(0, GBP)
         |^ 
         |""".stripMargin
    )
  }
}
