---
id: money
title: Money[T]
sidebar_label: Money[T]
---

**`Money[C]`** represents amount of money in a specific currency **`C`**.
It can be used as a stand-alone if you are only working with one type of money.
It can be used with [`MoneyTree[T]`](money-tree.md) to mix different currencies.

`Money[C]` used a `BigDecimal` for the amount so it may have more precision while doing calculations.


```scala
Money can be used in the following way:
> Money(1, USD) + Money(5, USD)
app.fmgp.money.Money[app.fmgp.money.Currency.USD.type] = Money(6,app.fmgp.money.Currency$USD)
```
```scala
> Money(100, USD) / 5
app.fmgp.money.Money[app.fmgp.money.Currency.USD.type] = Money(20,app.fmgp.money.Currency$USD)
```



:::caution
It's not possible to add different types of money!
The following code will intentional not compile:
```scala
> Money(50, USD) + Money(100, EUR)
error: Cannot prove that app.fmgp.money.Currency.EUR.type =:= app.fmgp.money.Currency.USD.type.
```
:::

You can find here the source code of [Money[T]][SourceCode]

[SourceCode]: https://github.com/FabioPinheiro/cats-money/blob/master/src/main/scala/app/fmgp/money/Money.scala  'Money[T] source code'
