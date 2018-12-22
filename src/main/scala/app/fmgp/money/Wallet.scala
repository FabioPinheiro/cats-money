package app.fmgp.money


import cats.syntax.monoid._
import cats.instances.bigDecimal._
import cats.instances.map._

import cats.kernel.{CommutativeGroup, Eq, Monoid}


sealed abstract case class Wallet[C](w: Map[C, BigDecimal]) {
  def get[T <: C](c: T): Option[(T, BigDecimal)] = {
    w.get(c).map((c, _))
  }
  def update(c: C, m:BigDecimal) = Wallet(w.updated(c,m))
}

object Wallet {
  def empty[C]: Wallet[C] = new Wallet(Map[C, BigDecimal]()) {}

  //def apply: Wallet = empty
  private[money] def apply[C](w: Map[C, BigDecimal]): Wallet[C] = new Wallet[C](w) {}

  implicit def eqv[T]: Eq[Wallet[T]] = Eq.instance((a, b) =>
    a.w.filterNot(_._2 == catsKernelStdGroupForBigDecimal.empty) == b.w.filterNot(_._2 == catsKernelStdGroupForBigDecimal.empty)
  )


  def fMoneyXCommutativeGroup[C]: CommutativeGroup[Wallet[C]] = new CommutativeGroup[Wallet[C]] {
    override def combine(x: Wallet[C], y: Wallet[C]): Wallet[C] = Wallet(x.w |+| y.w)

    override def empty: Wallet[C] = Wallet.empty[C]

    import cats.instances.bigDecimal.catsKernelStdGroupForBigDecimal

    override def inverse(a: Wallet[C]): Wallet[C] = Wallet(a.w.mapValues(e => catsKernelStdGroupForBigDecimal.inverse(e)))
  }


  object X {
    def fromMoney[C <: Enumeration#Value](m: MoneyX[C]): Wallet[C] = {
      if (m.amount == BigDecimal(0)) empty[C]
      else new Wallet(Map[C, BigDecimal](m.currency -> m.amount)) {}
    }

    implicit val moneyXCommutativeGroup: CommutativeGroup[Wallet[Currency.CurrencyX]] = fMoneyXCommutativeGroup[Currency.CurrencyX]
  }

  object Y {

    import app.fmgp.money.CurrencyY.CY

    def fromMoney[C <: CY](m: MoneyY[C]): Wallet[CY] = {
      if (m.amount == BigDecimal(0)) empty[CY]
      else new Wallet(Map[CY, BigDecimal](m.currency -> m.amount)) {}
    }

    implicit val moneyYCommutativeGroup: CommutativeGroup[Wallet[CurrencyY.CY]] = fMoneyXCommutativeGroup[CurrencyY.CY]
  }

}


