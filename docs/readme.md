# Cats Money

To install my project

```scala
libraryDependencies += "app.fmgp" % "cats-money" % "@VERSION@"
```

```scala mdoc
val x = 1
List(x, x)
```

```scala mdoc:fail
val typeError: Int = "should be int"
```

```scala
//TODO mdoc does not work for dotty
import app.fmgp.money._
import app.fmgp.money.Currency._

type CURRENCY = Currency.GBP.type | Currency.USD.type | Currency.EUR.type
//type CURRENCY = Currency.type
object Instances extends app.fmgp.money.instances.InstancesForCurrency[CURRENCY]
```

```scala
import Instances.all._{given _, _}

val a = Money(100, USD)
val b = Money(200, USD)
val c = Money(300, GBP)
val d = Money(9000, EUR)
val e = Money(1, JPY)

println(a)
println(s"Must be 300: ${a+b}")
//println(s"Must fail  : ${a+c}") //Must fail compilation
```