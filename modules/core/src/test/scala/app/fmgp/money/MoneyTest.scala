package app.fmgp.money

class MoneyTest extends munit.FunSuite {

  test("Money sum with different currency shound not compile") {
    val errorMsg = compileErrors("Money(1, EUR) + Money(2, GBP)")
    val expectedError = "Cannot prove that app.fmgp.money.GBP.type =:= app.fmgp.money.EUR.type"
    assert(errorMsg.contains(expectedError))
  }

  test("Money comparison with different currency must be false is 'scala.language.strictEquality' is disable") {
    assert(Money(0, EUR) != Money(0, GBP))
  }
  test("Money comparison with different currency shound not compile with 'scala.language.strictEquality'") {
    import scala.language.strictEquality
    assertNoDiff(
      compileErrors("Money(0, EUR) == Money(0, GBP)"),
      """|error:
         |Values of types app.fmgp.money.Money[app.fmgp.money.EUR.type] and app.fmgp.money.Money[app.fmgp.money.GBP.type] cannot be compared with == or !=.
         |I found:
         |
         |    app.fmgp.money.Money.derived$CanEqual[app.fmgp.money.EUR.type, 
         |      app.fmgp.money.GBP.type
         |    ](
         |      /* missing */
         |        summon[CanEqual[app.fmgp.money.EUR.type, app.fmgp.money.GBP.type]]
         |    )
         |
         |But no implicit values were found that match type CanEqual[app.fmgp.money.EUR.type, app.fmgp.money.GBP.type].
         |Money(0, EUR) == Money(0, GBP)
         |^ 
         |""".stripMargin
    )
  }
}
