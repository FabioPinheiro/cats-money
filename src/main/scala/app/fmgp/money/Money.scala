package app.fmgp.money

import cats.Show
sealed abstract case class MoneyY[+T](currency: T)
sealed abstract class MoneyTree[+T]
object MoneyTreeInstances {
  //Takes 50s to give a Type Mismatch Error on endlessCompilation
  //given MoneyTreeShow[T](using Show[T]) as Show[MoneyTree[T]] = ???

  //Takes more then 10 mins to give the  Type Mismatch Error on endlessCompilation
  implicit def MoneyTreeShow[T](implicit showT: Show[T]): Show[MoneyTree[T]] = ???
}
