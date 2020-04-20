package app.fmgp.money

import cats.Show

sealed abstract case class MoneyY[+T](currency: T)

sealed abstract class MoneyTree[+T]

object MoneyTreeInstances {
  implicit def MoneyTreeShow[T](implicit showT: Show[T]): Show[MoneyTree[T]] = ???
}
