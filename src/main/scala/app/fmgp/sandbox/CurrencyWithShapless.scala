package app.fmgp.sandbox

import app.fmgp.money.CurrencyY
import app.fmgp.money.instances.InstancesForCurrency
import shapeless.union.Union
import shapeless.{:+:, CNil}

object EUR_XXX {
  type CurencyList = CurrencyY.EUR.type :+: CurrencyY.XXX.type :+: CNil
  type Curency = Union.`'eur -> CurrencyY.EUR, 'usd -> CurrencyY.USD`.T
}

object CurrencyWithShapless {
  object CTest extends InstancesForCurrency[EUR_XXX.Curency]
}
