package app.fmgp.money

class CurrencyTest extends munit.FunSuite {
  test("Different currency comparison shound not compile") {
    // Set(2, 1).sorted
    assertNoDiff(
      compileErrors("EUR == GBP"),
      """|error: Values of types object app.fmgp.money.EUR and object app.fmgp.money.GBP cannot be compared with == or !=
         |EUR == GBP
         |^""".stripMargin
    )
  }
}
