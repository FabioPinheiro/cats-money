sealed trait Currency //derives CanEqual
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
//   object EUR extends Currency { given CanEqual[EUR.type, EUR.type] = CanEqual.derived }
//   object CHF extends Currency { given CanEqual[CHF.type, CHF.type] = CanEqual.derived }
// }
