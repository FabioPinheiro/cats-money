# Cats-Money

Scala functional library to deal amounts of money.
The goal is to provide basic mathematical reasoning to work in money currency and exchange rate.

###### Power by cats & shapeless:

![Cats Friendly Badge][cats-badge] & <img src="https://pbs.twimg.com/media/Ci-p9mmXAAAlPyx.jpg:small" width="80">


## Getting Started

You will need to have Git, Java 8, and [SBT][sbt] installed.

You can also start a *Scala console from in SBT* (`scala>` prompt)
to play with small snippets of code:

```
import cats._, implicits._
import shapeless._, record._, union._, syntax.singleton._

import app.fmgp.money._
import app.fmgp.money.MoneyY._
import app.fmgp.money.MoneyYMonoid._
import app.fmgp.money.Wallet._
import app.fmgp.money.Wallet.Y._
import app.fmgp.money.CurrencyY._
```


### Adopters
This is still only a draft and experimental library.

### Maintainers
The current maintainers (people who can merge pull requests) are:

 * *Fabio Pinheiro* - [BitBucket](https://bitbucket.org/FabioPinheiro/) / [GitHub](https://github.com/FabioPinheiro)

#### TODO LIST
  * implement the rates conversion
  * rename classes and draw a UML
  * currency
    * add all currencies from ISO-4217
    * create subset type of currencies
      * try [shapeless][shapeless] Union type
        ```
        type U = Union.`'a -> EUR, 'b -> USD`.T
        val u1 = Coproduct[U]('a ->> EUR)
        println("u1", u1)
        ```
      * try [Dotty Union Types](https://dotty.epfl.ch/docs/reference/union-types.html) =)
    * implement the rates conversion on a subset of currencies
      * try [shapeless][shapeless] polymorphic function
        ```
        object polymorphicF extends Poly1 {
          implicit def caseEUR = at[EUR.type](i => "sEUR")
          implicit def caseSUSD = at[USD.type](s => "sUSD")
        }
        type CU = EUR.type :+: USD.type :+: CNil
        val cu = Coproduct[CU](USD)
        println("cu", cu, cu map polymorphicF)
        ```
  * *TESTS:*
    * FIX: MoneyTree.monad.tailRecM stack safety *** FAILED *** */
      * just need to store the structure when doing the loop (see code)
    * testing for non-compilation of type unsafe
      * [shapeless.test.illTyped][shapeless]
  * *DEMO:*
    * Use the cats's Writer Monad to make log about the conversion


### Copyright and License

cats-money is licensed under the MIT license, available at
http://opensource.org/licenses/mit-license.php and also in the
[LICENSE](LICENSE) file.

Copyright the Fabio Pinheiro, 2018.


[cats-badge]: https://typelevel.org/cats/img/cats-badge-tiny.png
[book]: https://underscore.io/books/advanced-scala
[license]: https://creativecommons.org/publicdomain/zero/1.0/
[sbt]: http://scala-sbt.org
[scala-ide]: http://scala-ide.org
[scala-metals]: https://scalameta.org/metals/
[shapeless]: https://github.com/milessabin/shapeless
