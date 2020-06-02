---
id: getting-started
title: Getting started
sidebar_label: Getting started
---

## Quick start

Cats-Money is a Scala functional library with the following goals:

* **Type-safe** manner to deal with amounts of money in different currencies.
* Provide **basic mathematical reasoning** to work in money currency and exchange rate.
* **Traceability** properties to make reports and have an easy way to debug.

---

## Lib

:::important
This library is still not published on Maven so for now you need to do a `publishLocal`. (See below)
:::


```
libraryDependencies += "app.fmgp" %% "cats-money" % "0.1.0"
//TODO Use %%% for non-JVM projects.
```

At the moment the project is being built with dotty version `0.24.0-RC1`.

| Scala Version          | JVM | Scala.js (1.x) |
| ---------------------- | :-: | :------------: |
| 2.13.x                 | n/a |       n/a      |
| 0.24.0-RC1             | ✅  |       n/a      |


### Publish Local

```scala
$ sbt
> publishLocal
```


### Run tests in sbt

Execute sbt test to run Cats-Money tests in the terminal. It's recommended to stay in the sbt shell for the best compiler performance.

```
$ sbt
> test
```


### Stability

Cats_money is a new library with no stability guarantees. Written in a language Dotty (future Scala 3) that is still not stable. While this project is versioned at v0.x, it's expected that new releases, including patch releases, will have binary and source breaking changes.