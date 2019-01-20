package app.fmgp.sandbox

import app.fmgp.money.{CurrencyY, MoneyY}
import cats.instances.bigDecimal.catsKernelStdGroupForBigDecimal
import cats.instances.map._
import cats.kernel.{CommutativeGroup, Eq}
import cats.syntax.monoid._

sealed abstract case class Wallet[C](w: Map[C, BigDecimal]) {
  def get[T <: C](c: T): Option[(T, BigDecimal)] = {
    w.get(c).map((c, _))
  }
  def update(c: C, m: BigDecimal) = Wallet(w.updated(c, m))
}

object Wallet {
  def empty[C]: Wallet[C] = new Wallet(Map[C, BigDecimal]()) {}

  //def apply: Wallet = empty
  private[sandbox] def apply[C](w: Map[C, BigDecimal]): Wallet[C] = new Wallet[C](w) {}

  implicit def eqv[T]: Eq[Wallet[T]] = Eq.instance { (a, b) =>
    val aWithZeroValues = a.w.filterNot(_._2 == catsKernelStdGroupForBigDecimal.empty)
    val bWithZeroValues = b.w.filterNot(_._2 == catsKernelStdGroupForBigDecimal.empty)
    aWithZeroValues == bWithZeroValues
  }


  def fMoneyXCommutativeGroup[C]: CommutativeGroup[Wallet[C]] = new CommutativeGroup[Wallet[C]] {
    override def combine(x: Wallet[C], y: Wallet[C]): Wallet[C] = Wallet(x.w |+| y.w)

    override def empty: Wallet[C] = Wallet.empty[C]

    override def inverse(a: Wallet[C]): Wallet[C] = Wallet(a.w.mapValues(e => catsKernelStdGroupForBigDecimal.inverse(e)))
  }


  //TODO move this to the sandbox
  object X {
    def fromMoney[C <: Enumeration#Value](m: MoneyX[C]): Wallet[C] = {
      if (m.amount == BigDecimal(0)) empty[C]
      else new Wallet(Map[C, BigDecimal](m.currency -> m.amount)) {}
    }

    implicit val moneyXCommutativeGroup: CommutativeGroup[Wallet[CurrencyX.CurrencyX]] = fMoneyXCommutativeGroup[CurrencyX.CurrencyX]
  }

  object Y {

    def fromMoney[C <: CurrencyY.CY](m: MoneyY[C]): Wallet[CurrencyY.CY] = {
      if (m.amount == BigDecimal(0)) empty[CurrencyY.CY]
      else new Wallet(Map[CurrencyY.CY, BigDecimal](m.currency -> m.amount)) {}
    }

    implicit val moneyYCommutativeGroup: CommutativeGroup[Wallet[CurrencyY.CY]] = fMoneyXCommutativeGroup[CurrencyY.CY]
  }

}


