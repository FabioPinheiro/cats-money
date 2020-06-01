package app.fmgp.money

import app.fmgp.money.Currency._
import org.scalacheck.{Arbitrary, Cogen, Gen}

trait MoneyTestUtils {
  implicit def arbitraryCurrency: Arbitrary[CY] = Arbitrary(Gen.oneOf(Seq(USD, GBP, EUR)))
  implicit val cogenCurrencyX: Cogen[CY] = Cogen[String].contramap(_.toString)
  implicit def cogenMoney: Cogen[Money[CY]] = Cogen[String].contramap(_.toString)
  implicit def cogenMoneyTree: Cogen[MoneyTree[Money[CY]]] = Cogen[String].contramap(_.toString)

  def arbitraryAmount = Gen.chooseNum(-100d, 100d, 0, 1).map(BigDecimal.apply)

  implicit def arbitraryMoney_CY: Arbitrary[Money[CY]] =
    Arbitrary(
      for (amount <- arbitraryAmount; currency <- arbitraryCurrency.arbitrary) yield Money[CY](amount, currency)
    )

  def fArbitraryMoney[C <: CY](c: C): Arbitrary[Money[C]] =
    Arbitrary(for (amount <- arbitraryAmount) yield Money[C](amount, c))

  def arbitraryOneMoneyK: Arbitrary[MoneyK[CY]] =
    Arbitrary(
      for (amount <- arbitraryAmount; currency <- arbitraryCurrency.arbitrary) yield MoneyK[CY](Map(currency -> amount))
    )

  implicit def arbitraryMoneyK: Arbitrary[MoneyK[CY]] =
    Arbitrary(
      for (stream <- Gen.infiniteStream(arbitraryOneMoneyK.arbitrary)) yield stream.take(10).reduce((a, b) => a ++ b)
    )

  implicit val arbitraryMoneyXXX: Arbitrary[Money[XXX.type]] = fArbitraryMoney(XXX)
  implicit val arbitraryMoneyUSD: Arbitrary[Money[USD.type]] = fArbitraryMoney(USD)
  implicit val arbitraryMoneyEUR: Arbitrary[Money[EUR.type]] = fArbitraryMoney(EUR)
  implicit def cogenMoneyXXX: Cogen[Money[XXX.type]] = Cogen[String].contramap(_.toString)
  implicit def cogenMoneyUSD: Cogen[Money[USD.type]] = Cogen[String].contramap(_.toString)
  implicit def cogenMoneyEUR: Cogen[Money[EUR.type]] = Cogen[String].contramap(_.toString)

  implicit def arbitraryMoneyTree: Arbitrary[MoneyTree[Money[CY]]] =
    Arbitrary(for {
      money <- Gen.infiniteStream(arbitraryMoney_CY.arbitrary)
      aux = money.take(10).map(MoneyTree.one)
    } yield MoneyTree.branch(aux))

  implicit def arbitraryMoneyTreeXXX: Arbitrary[MoneyTree[Money[XXX.type]]] =
    Arbitrary(for {
      money <- Gen.infiniteStream(fArbitraryMoney(XXX).arbitrary)
      aux = money.take(10).map(MoneyTree.one)
    } yield MoneyTree.branch(aux))

  implicit def arbitraryMoneyTreeEUR: Arbitrary[MoneyTree[Money[EUR.type]]] =
    Arbitrary(for {
      money <- Gen.infiniteStream(fArbitraryMoney(EUR).arbitrary)
      aux = money.take(10).map(MoneyTree.one)
    } yield MoneyTree.branch(aux))

  //FIXME need
  def cToEUR = PartialRateConverter[CY, EUR.type](EUR, Map(USD -> 1.2, GBP -> 0.9))
  def cToXXX = PartialRateConverter[CY, XXX.type](XXX, Map(EUR -> 1.2))

  implicit val iArbFAtoB: Arbitrary[MoneyTree[Money[CY] => Money[EUR.type]]] = {
    def aaa(a: Money[CY]): Money[EUR.type] = cToEUR.convert(a) //FIXME ...
    Arbitrary(Gen.const(MoneyLeaf(aaa)))
  }

  implicit val ArbFBtoC: Arbitrary[MoneyTree[Money[EUR.type] => Money[XXX.type]]] = {
    def aaa(a: Money[EUR.type]): Money[XXX.type] = cToXXX.convert(a) //FIXME ...
    Arbitrary(Gen.const(MoneyLeaf(aaa)))
  }

}
