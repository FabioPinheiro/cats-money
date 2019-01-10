package app.fmgp.money

import app.fmgp.money.CurrencyY._
import org.scalacheck.{Arbitrary, Cogen, Gen}

trait MoneyTestUtils {
  implicit def arbitraryCurrency: Arbitrary[CY] = Arbitrary(Gen.oneOf(Seq(USD, GBP, EUR)))
  implicit val cogenCurrencyX: Cogen[CY] = Cogen[String].contramap(_.toString)
  implicit def cogenMoneyY: Cogen[MoneyY[CY]] = Cogen[String].contramap(_.toString)
  implicit def cogenMoneyTree: Cogen[MoneyTree[MoneyY[CY]]] = Cogen[String].contramap(_.toString)

  def arbitraryAmount = Gen.chooseNum(-100d, 100d, 0, 1).map(BigDecimal.apply)

  implicit def arbitraryMoneyY_CY: Arbitrary[MoneyY[CY]] =
    Arbitrary(for (amount <- arbitraryAmount; currency <- arbitraryCurrency.arbitrary) yield MoneyY[CY](amount, currency))

  def fArbitraryMoney[C <: CY](c: C): Arbitrary[MoneyY[C]] =
    Arbitrary(for (amount <- arbitraryAmount) yield MoneyY[C](amount, c))

  def arbitraryOneMoneyK: Arbitrary[MoneyK[CY]] =
    Arbitrary(for (amount <- arbitraryAmount; currency <- arbitraryCurrency.arbitrary) yield MoneyK[CY](Map(currency -> amount)))

  implicit def arbitraryMoneyK: Arbitrary[MoneyK[CY]] =
    Arbitrary(for (stream <- Gen.infiniteStream(arbitraryOneMoneyK.arbitrary)) yield stream.take(10).reduce((a, b) => a ++ b))

  implicit val arbitraryMoneyXXX: Arbitrary[MoneyY[XXX.type]] = fArbitraryMoney(XXX)
  implicit val arbitraryMoneyUSD: Arbitrary[MoneyY[USD.type]] = fArbitraryMoney(USD)
  implicit val arbitraryMoneyEUR: Arbitrary[MoneyY[EUR.type]] = fArbitraryMoney(EUR)
  implicit def cogenMoneyYXXX: Cogen[MoneyY[XXX.type]] = Cogen[String].contramap(_.toString)
  implicit def cogenMoneyYUSD: Cogen[MoneyY[USD.type]] = Cogen[String].contramap(_.toString)
  implicit def cogenMoneyYEUR: Cogen[MoneyY[EUR.type]] = Cogen[String].contramap(_.toString)

  implicit def arbitraryMoneyTree: Arbitrary[MoneyTree[MoneyY[CY]]] = Arbitrary(for {
    money <- Gen.infiniteStream(arbitraryMoneyY_CY.arbitrary)
    aux = money.take(10).map(MoneyTree.one)
  } yield MoneyTree.branch(aux))

  implicit def arbitraryMoneyTreeXXX: Arbitrary[MoneyTree[MoneyY[XXX.type]]] = Arbitrary(for {
    money <- Gen.infiniteStream(fArbitraryMoney(XXX).arbitrary)
    aux = money.take(10).map(MoneyTree.one)
  } yield MoneyTree.branch(aux))

  implicit def arbitraryMoneyTreeEUR: Arbitrary[MoneyTree[MoneyY[EUR.type]]] = Arbitrary(for {
    money <- Gen.infiniteStream(fArbitraryMoney(EUR).arbitrary)
    aux = money.take(10).map(MoneyTree.one)
  } yield MoneyTree.branch(aux))


  //FIXME need
  def cToEUR = PartialRateConverter[CY, EUR.type](EUR, Map(USD -> 1.2, GBP -> 0.9))
  def cToXXX = PartialRateConverter[CY, XXX.type](XXX, Map(EUR -> 1.2))


  implicit val iArbFAtoB: Arbitrary[MoneyTree[MoneyY[CY] => MoneyY[EUR.type]]] = {
    def aaa(a: MoneyY[CY]): MoneyY[EUR.type] = cToEUR.convert(a) //FIXME ...
    Arbitrary(Gen.const(MoneyZLeaf(aaa)))
  }

  implicit val ArbFBtoC: Arbitrary[MoneyTree[MoneyY[EUR.type] => MoneyY[XXX.type]]] = {
    def aaa(a: MoneyY[EUR.type]): MoneyY[XXX.type] = cToXXX.convert(a) //FIXME ...
    Arbitrary(Gen.const(MoneyZLeaf(aaa)))
  }

}
