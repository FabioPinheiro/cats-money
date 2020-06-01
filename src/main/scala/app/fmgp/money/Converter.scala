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

/**
  * C is supose to be a union type
  * Try to use https://dotty.epfl.ch/docs/reference/new-types/match-types.html
  * @throws scala.MatchError at runtime is a needed rate is missing
  */
case class PartialRateConverter[C, T <: C](to: T, rates: Map[C, BigDecimal]) extends Converter[Money[C], Money[T]] {
  def tToT(to: T): PartialFunction[Money[C], Logged[Money[T]]] = {
    case Money(a, `to`) => Money[T](a, to).pure[Logged] //m.asInstanceOf[Money[T]] //THIS WAS A SAFE CAST
  }
  val pf: PartialFunction[Money[C], Logged[Money[T]]] = {
    val ii = rates.filterNot(_._1 == to).map {
      case (from, rate) =>
        val pf: PartialFunction[Money[C], Logged[Money[T]]] = {
          case Money(amount, `from`) =>
            Money[T](amount * rate, to).writer(Vector(s"$amount$from($rate)->${amount * rate}$to"))
        }
        pf
    }
    tToT(to) orElse ii.reduce((a, b) => a orElse b)
  }

  /** @throws MatchError case a conversion rate is missing () */
  override def convert(money: Money[C]): Money[T] = apply(money).value
  override def convertWithLog(money: Money[C]): Logged[Money[T]] = apply(money)
  //def convertOption(money: Money[C]): Option[Money[T]] = if (isDefinedAt(money)) Some(apply(money)) else None
  def isDefinedAt(money: Money[C]): Boolean = pf.isDefinedAt(money)
  //TODO def isDefinedAt[F[_]](money: F[Money[C]])(f: Functor[F]): Boolean = f.map(money)(e => isDefinedAt(e))
  def apply(money: Money[C]): Logged[Money[T]] = pf(money)
}
