# Cats-Money

Scala functional library to deal amounts of money.
The goal is to provide basic mathematical reasoning to work in money currency and exchange rate.

[![Build Status](https://travis-ci.com/FabioPinheiro/cats-money.svg?branch=master)](https://travis-ci.com/FabioPinheiro/cats-money)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/9e9fffffdf4e45a7b6c99bb4939a0ce3)](https://app.codacy.com/app/fabiomgpinheiro/cats-money?utm_source=github.com&utm_medium=referral&utm_content=FabioPinheiro/cats-money&utm_campaign=Badge_Grade_Dashboard)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/05e27a1b2c8e4f0a88183150ce3e9416)](https://www.codacy.com/app/fabiomgpinheiro/cats-money?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=FabioPinheiro/cats-money&amp;utm_campaign=Badge_Coverage)

## Power by cats & shapeless:

![Cats Friendly Badge][cats-badge]
&
<img src="https://pbs.twimg.com/media/Ci-p9mmXAAAlPyx.jpg:small" width="80">

## Getting Started

You will need to have Git, Java 8, and [SBT][sbt] installed.

We try to have a similar structure to the cats library.
Have a look of the [Cats Infographic][cats-infographic] from tpolecat
and to the [Underscore's Scala Book][underscore-scala-book].

You can also start a *Scala console from in SBT* (`scala>` prompt)
to play with small snippets of code:

```scala
import cats._, implicits._
import shapeless._, record._, union._, syntax.singleton._
import app.fmgp.money.instances.all._

import app.fmgp.money._
import app.fmgp.money.MoneyY._
import app.fmgp.money.CurrencyY._
```

Have a look on the to *Demos* for more examples (on the test folder):
* `sbt "test:runMain app.fmgp.money.Demo"`
* `sbt "test:runMain app.fmgp.money.Main"`

### Adopters
This is still only a draft and experimental library.
But I plane to use this on the near future.
If anyone else is also looking for a functional approach to work with monetary value, send me a messagem.
(I would appreciate example use cases to better reflect over the library.)

### Maintainers
The current maintainers (people who can merge pull requests) are:

* *Fabio Pinheiro* - [BitBucket](https://bitbucket.org/FabioPinheiro/) / [GitHub](https://github.com/FabioPinheiro)

#### TODO LIST
* implement:
  * [ ] Traverse\[MoneyTree\]
* rename classes and draw a UML
  * [X] rename MoneyY and CurrencyY files
* currency
  * [ ] add all currencies from ISO-4217
  * create subset type of currencies
    * [ ] try [shapeless][shapeless] Union type
      ```scala
      type U = Union.`'a -> EUR, 'b -> USD`.T
      val u1 = Coproduct[U]('a ->> EUR)
      println("u1", u1)
      ```
    * [ ] try [Dotty Union Types](https://dotty.epfl.ch/docs/reference/union-types.html) =)
  * implement the rates conversion on a subset of currencies
    * [ ] try [shapeless][shapeless] polymorphic function
      ```scala
      object polymorphicF extends Poly1 {
        implicit def caseEUR = at[EUR.type](i => "sEUR")
        implicit def caseSUSD = at[USD.type](s => "sUSD")
      }
      type CU = EUR.type :+: USD.type :+: CNil
      val cu = Coproduct[CU](USD)
      println("cu", cu, cu map polymorphicF)
      ```
* tests:
  * [ ] Testing for non-compilation of type unsafe
    * [shapeless.test.illTyped][shapeless]
  * [X] Add a code coverage tool (sbt-scoverage)
* demo:
  * [X] (+- Done) Demo Main
* to try:
  * [ ] Epimorphism, Monomorphism and Isomorphism
  * [X] Rings +-

### Copyright and License

cats-money is licensed under the MIT license, available at
[http://opensource.org/licenses/mit-license.php](http://opensource.org/licenses/mit-license.php)
and also in the [LICENSE](LICENSE) file.

Copyright the Fabio Pinheiro, 2018.

[cats-badge]: https://typelevel.org/cats/img/cats-badge-tiny.png
[cats-infographic]: https://github.com/tpolecat/cats-infographic
[underscore-scala-book]: https://underscore.io/books/advanced-scala
[sbt]: http://scala-sbt.org
[shapeless]: https://github.com/milessabin/shapeless
