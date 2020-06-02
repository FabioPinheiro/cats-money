---
id: instances
title: Instances
sidebar_label: Instances
---

In order to for the full set of operations available you must create a singleton instance of **`InstancesForCurrency[C]`**.
Where **`C`** is your set of currencies.
It is possible to override any of the behaviors when missing with if you wish.


```scala
object Instances extends app.fmgp.money.instances.InstancesForCurrency[MyCurrency]

import Instances.all.{given _, _}
```

## Defualts
You can also use the [**ISO_4217 list currencies**](currency).
In that case you only need to import the defualt defualt instances.
```scala
import app.fmgp.money.instances.all.{given _, _}
```

[**`InstancesForCurrency[C]`**][SourceCode-packageInstances] mix the instances from [**`MoneyInstances`**][SourceCode-moneyInstances] with [**`MoneyTreeInstances`**][SourceCode-moneyTreeInstances].
For more customization, you can mix those yourself.


[SourceCode-packageInstances]: https://github.com/FabioPinheiro/cats-money/blob/master/src/main/scala/app/fmgp/money/instances/package.scala  'Package Instances '
[SourceCode-moneyInstances]: https://github.com/FabioPinheiro/cats-money/blob/master/src/main/scala/app/fmgp/money/instances/MoneyInstances.scala  'MoneyInstances'
[SourceCode-moneyTreeInstances]: https://github.com/FabioPinheiro/cats-money/blob/master/src/main/scala/app/fmgp/money/instances/MoneyTreeInstances.scala  'MoneyTreePackage'