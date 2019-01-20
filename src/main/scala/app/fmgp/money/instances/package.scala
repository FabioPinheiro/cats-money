package app.fmgp.money

package object instances {
  //object all extends AllInstances with AllInstancesBinCompat0 with AllInstancesBinCompat1 with AllInstancesBinCompat2
  object all extends MoneyInstances with MoneyTreeInstances
  object money extends MoneyInstances
  object moneyTree extends MoneyTreeInstances

  trait InstancesForCurrency[CURRENCY] {
    type C = CURRENCY
    object all extends MoneyInstancesC[C] with MoneyTreeInstances
    object money extends MoneyInstancesC[C]
    object moneyTree extends MoneyTreeInstances
  }

  object CY extends InstancesForCurrency[app.fmgp.money.CurrencyY.CY]
}
