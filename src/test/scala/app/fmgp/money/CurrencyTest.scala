package app.fmgp.money

import app.fmgp.money.Currency._

class CurrencyTest extends munit.FunSuite {
  test("Different currency comparison shound not compile") {
    // Set(2, 1).sorted
    assertNoDiff(
      compileErrors("EUR == GBP"),
      """|error: Values of types object app.fmgp.money.Currency.EUR and object app.fmgp.money.Currency.GBP cannot be compared with == or !=
         |EUR == GBP
         |^""".stripMargin
    )
  }
}
