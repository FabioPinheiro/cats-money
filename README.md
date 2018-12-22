# Cats-Money

Scala functional library to deal amounts of money.
The goal is to provide basic mathematical reasoning to work in money currency and exchange rate.

###### Power by cats:

![Cats Friendly Badge][cats-badge] 


## Getting Started

You will need to have Git, Java 8, and [SBT][sbt] installed.

You can also start a *Scala console from in SBT* (`scala>` prompt)
to play with small snippets of code:

```
import cats._
import cats.implicits._
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

### Copyright and License

cats-money is licensed under the MIT license, available at
http://opensource.org/licenses/mit-license.php and also in the
[LICENSE](LICENSE) file.

Copyright the maintainers, 2018.


[cats-badge]: https://typelevel.org/cats/img/cats-badge-tiny.png
[book]: https://underscore.io/books/advanced-scala
[license]: https://creativecommons.org/publicdomain/zero/1.0/
[sbt]: http://scala-sbt.org
[scala-ide]: http://scala-ide.org
[scala-metals]: https://scalameta.org/metals/
