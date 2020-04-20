package app.fmgp.money

import cats.syntax.all._
import app.fmgp.money._
import MoneyTreeInstances._

object Demo extends App {

  type AUX = Int | Unit

  //FIXME
  val maybeBug: MoneyTree[MoneyY[AUX]] = (MoneyY(100, Int): AUX).pure[MoneyTree]

}
