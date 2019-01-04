package app.fmgp.money

import cats.data.Writer
import cats.instances.vector._
import cats.syntax.writer._
import cats.syntax.applicative._

trait Converter[-FROM, TO] {
  def convert(from: FROM): TO
  type Logged[A] = Writer[Vector[String], A]
  def convertWithLog(from: FROM): Logged[TO]
}

//TODO REMOVE THIS WILL NOT WORK
case class ShapelessConverter[T <: EUR_XXX.type](eur: Double, xxx: Double) extends Converter[MoneyY[EUR_XXX.type], MoneyY[T]] {

  object poly extends shapeless.Poly1 {
    implicit def caseEUR = at[CurrencyY.EUR.type](s => eur)
    implicit def caseXXX = at[CurrencyY.XXX.type](s => xxx)
  }

  def convertTODO(money: MoneyY[CurrencyY.EUR.type]): MoneyY[T] = {
    val pf = poly(money.currency)
    ??? //MoneyY(money.amount * pf, T)
  }

  def convert(money: MoneyY[EUR_XXX.type]): MoneyY[T] = ???
  override def convertWithLog(from: MoneyY[EUR_XXX.type]): Logged[MoneyY[T]] = ???
}

case class PartialRateConverter[C, T <: C](to: T, rates: Map[C, BigDecimal]) extends Converter[MoneyY[C], MoneyY[T]] {
  def tToT(to: T): PartialFunction[MoneyY[C], Logged[MoneyY[T]]] = {
    case MoneyY(a, `to`) => MoneyY[T](a, to).pure[Logged] //m.asInstanceOf[MoneyY[T]] //THIS WAS A SAFE CAST
  }
  val pf: PartialFunction[MoneyY[C], Logged[MoneyY[T]]] = {
    val ii = rates.filterNot(_._1 == to).map {
      case (from, rate) =>
        val pf: PartialFunction[MoneyY[C], Logged[MoneyY[T]]] = {
          case MoneyY(amount, `from`) => MoneyY[T](amount * rate, to).writer(Vector(s"$amount$from($rate)->${amount * rate}$to"))
        }
        pf
    }
    tToT(to) orElse ii.reduce((a, b) => a orElse b)
  }

  /** @throws MatchError case a conversion rate is missing () */
  override def convert(money: MoneyY[C]): MoneyY[T] = apply(money).value
  override def convertWithLog(money: MoneyY[C]): Logged[MoneyY[T]] = apply(money)
  //def convertOption(money: MoneyY[C]): Option[MoneyY[T]] = if (isDefinedAt(money)) Some(apply(money)) else None
  def isDefinedAt(money: MoneyY[C]): Boolean = pf.isDefinedAt(money)
  //TODO def isDefinedAt[F[_]](money: F[MoneyY[C]])(f: Functor[F]): Boolean = f.map(money)(e => isDefinedAt(e))
  def apply(money: MoneyY[C]): Logged[MoneyY[T]] = pf(money)
}
