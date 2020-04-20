package app.fmgp.money

import cats.{Show, Functor, Monad, Traverse}

sealed abstract case class MoneyY[+T](currency: T)

sealed abstract class MoneyTree[+T]

object MoneyTreeInstances {
  implicit def MoneyTreeShow[T](implicit showT: Show[T]): Show[MoneyTree[T]] = ???
  implicit val MoneyTreeFunctor: Functor[MoneyTree] = ???
  implicit val MoneyTreeMonad: Monad[MoneyTree] = ???
  val MoneyTreeTraverse: Traverse[MoneyTree] = ???
}
