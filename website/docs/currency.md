---
id: currency
title: Currency
sidebar_label: Currency
---

**`Currency`** is a sealed trait. Where each case represents one currency of the ISO_4217 currencies list.

You can use `Currency` like:
* the full of currencies here.
* implement your on list of Currency
* select a subset of this list


#### Create a list of Currency
```scala
sealed trait MyCurrency
object Currency {
  object GBP extends Currency { given Eql[GBP.type, GBP.type] = Eql.derived }
  object EUR extends Currency { given Eql[EUR.type, EUR.type] = Eql.derived }
  object USD extends Currency { given Eql[USD.type, USD.type] = Eql.derived }
```

#### Select a subset of Currencies
```scala
import app.fmgp.money.Currency.{GBP, EUR, USD}
type MyCurrency = Currency.GBP.type | Currency.USD.type | Currency.EUR.type
```

You can find here the source code of [Currency][SourceCode]

[SourceCode]: https://github.com/FabioPinheiro/cats-money/blob/master/src/main/scala/app/fmgp/money/Currency.scala  'Currency source code'