sealed trait Currency //derives Eql
object Currency {
  object EUR extends Currency
  object CHF extends Currency
}

/*
Frist try to compile and then:
Changed the above test to this still compiles.
The problem is that the Demo should recompile
 */

// sealed trait Currency
// object Currency {
//   object EUR extends Currency { given Eql[EUR.type, EUR.type] = Eql.derived }
//   object CHF extends Currency { given Eql[CHF.type, CHF.type] = Eql.derived }
// }
