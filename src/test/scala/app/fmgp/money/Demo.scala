package app.fmgp.money

import cats.syntax.all._
import app.fmgp.money.instances._

object all extends MoneyInstances with MoneyTreeInstances
//object all extends MoneyTreeInstances
object Demo extends App {

  enum CCC {
  case EUR 
  case GBP 
  case USD 
  }

  type AUX = CCC.EUR.type | CCC.GBP.type | CCC.USD.type

  import all._

  val a = MoneyY(100, CCC.USD)
  val b = MoneyY(200, CCC.USD)
  val c = MoneyY(300, CCC.GBP)
  val d = MoneyY(9000, CCC.EUR)

  val ok: MoneyTree[MoneyY[CCC]] = a.pure[MoneyTree] :+ b :+ c :+ d

  //FIXME
  val maybeBug: MoneyTree[MoneyY[AUX]] = (a: AUX).pure[MoneyTree] :+ b :+ c :+ d

}
