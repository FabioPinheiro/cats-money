package app.fmgp.money

import app.fmgp.money.CurrencyY._
import app.fmgp.money.MoneyYMonoid.fMoneyYMonoid
import org.scalacheck.{Arbitrary, Cogen, Gen}

trait MoneyTestUtils {

  implicit val moneyMonoidXXX = fMoneyYMonoid(XXX)

  implicit def arbitraryCurrency: Arbitrary[CY] = Arbitrary(Gen.oneOf(Seq(USD, GBP, EUR)))
  implicit val cogenCurrencyX: Cogen[CY] = Cogen[String].contramap(_.toString)
  implicit def cogenMoneyY: Cogen[MoneyY[CY]] = Cogen[String].contramap(_.toString)
  implicit def cogenMoneyTree: Cogen[MoneyTree[MoneyY[CY]]] = Cogen[String].contramap(_.toString)


  implicit def arbitraryMoneyX: Arbitrary[MoneyY[CY]] = Arbitrary(for {
    amount <- Gen.chooseNum(-100d, 100d, 0, 1).map(BigDecimal.apply)
    currency <- arbitraryCurrency.arbitrary
  } yield MoneyY[CY](amount, currency))

  def fArbitraryMoney[C <: CY](c: C): Arbitrary[MoneyY[C]] = Arbitrary(for {
    amount <- Gen.chooseNum(-100d, 1000, 0, 1).map(BigDecimal.apply)
  } yield MoneyY[C](amount, c))

  implicit val arbitraryMoneyXXX: Arbitrary[MoneyY[XXX.type]] = fArbitraryMoney(XXX)
  implicit val arbitraryMoneyUSD: Arbitrary[MoneyY[USD.type]] = fArbitraryMoney(USD)
  implicit val arbitraryMoneyEUR: Arbitrary[MoneyY[EUR.type]] = fArbitraryMoney(EUR)
  implicit def cogenMoneyYXXX: Cogen[MoneyY[XXX.type]] = Cogen[String].contramap(_.toString)
  implicit def cogenMoneyYUSD: Cogen[MoneyY[USD.type]] = Cogen[String].contramap(_.toString)
  implicit def cogenMoneyYEUR: Cogen[MoneyY[EUR.type]] = Cogen[String].contramap(_.toString)

  implicit def arbitraryMoneyTree: Arbitrary[MoneyTree[MoneyY[CY]]] = Arbitrary(for {
    money <- Gen.infiniteStream(arbitraryMoneyX.arbitrary)
    aux = money.take(10).map(MoneyTree.leaf)
  } yield MoneyTree.branch(aux))

  implicit def arbitraryMoneyTreeXXX: Arbitrary[MoneyTree[MoneyY[XXX.type]]] = Arbitrary(for {
    money <- Gen.infiniteStream(fArbitraryMoney(XXX).arbitrary)
    aux = money.take(10).map(MoneyTree.leaf)
  } yield MoneyTree.branch(aux))

  implicit def arbitraryMoneyTreeEUR: Arbitrary[MoneyTree[MoneyY[EUR.type]]] = Arbitrary(for {
    money <- Gen.infiniteStream(fArbitraryMoney(EUR).arbitrary)
    aux = money.take(10).map(MoneyTree.leaf)
  } yield MoneyTree.branch(aux))


  //FIXME need
  def cToEUR = PartialRateConverter.fromMapRates[CY,EUR.type](EUR, Map(USD->1.2, GBP->0.9))
  def cToXXX = PartialRateConverter.fromMapRates[CY,XXX.type](XXX, Map(EUR->1.2))




  implicit val iArbFAtoB: Arbitrary[MoneyTree[MoneyY[CY] => MoneyY[EUR.type]]] = {
    def aaa(a: MoneyY[CY]): MoneyY[EUR.type] = cToEUR.convert(a) //FIXME ...
    Arbitrary(Gen.const(MoneyZLeaf(aaa)))
  }

  implicit val ArbFBtoC:Arbitrary[MoneyTree[MoneyY[EUR.type] => MoneyY[XXX.type]]] = {
    def aaa(a: MoneyY[EUR.type]): MoneyY[XXX.type] = cToXXX.convert(a) //FIXME ...
    Arbitrary(Gen.const(MoneyZLeaf(aaa)))
  }

}
