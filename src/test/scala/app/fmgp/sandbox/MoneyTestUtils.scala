package app.fmgp.sandbox

import app.fmgp.money.CurrencyY.{CY, CYValues, CurrencyY, EUR, USD, XXX}
import app.fmgp.money.MoneyYMonoid.fMoneyYMonoid
import app.fmgp.money.{CurrencyY, MoneyTree, MoneyY}
import org.scalacheck.{Arbitrary, Cogen, Gen}

trait MoneyTestUtils {

  implicit val moneyMonoidXXX = fMoneyYMonoid(XXX)

  implicit def arbitraryCurrency: Arbitrary[CY] = Arbitrary(Gen.oneOf(CYValues))
  implicit val cogenCurrencyX: Cogen[CY] = Cogen[String].contramap(_.toString)
  implicit def cogenMoneyY: Cogen[MoneyY[CY]] = Cogen[String].contramap(_.toString)
  implicit def cogenMoneyTree: Cogen[MoneyTree[MoneyY[CY]]] = Cogen[String].contramap(_.toString)


  implicit def arbitraryMoneyX: Arbitrary[MoneyY[CY]] = Arbitrary(for {
    amount <- Gen.chooseNum(-100d, 100d, 0, 1).map(BigDecimal.apply)
    currency <- arbitraryCurrency.arbitrary
  } yield MoneyY[CY](amount, currency))

  def fArbitraryMoney[C <: CurrencyY](c: C): Arbitrary[MoneyY[C]] = Arbitrary(for {
    amount <- Gen.chooseNum(-100d, 1000, 0, 1).map(BigDecimal.apply)
  } yield MoneyY[C](amount, c))

  implicit val arbitraryMoneyXXX: Arbitrary[MoneyY[CurrencyY.XXX.type]] = fArbitraryMoney(XXX)
  implicit val arbitraryMoneyUSD: Arbitrary[MoneyY[CurrencyY.USD.type]] = fArbitraryMoney(USD)
  implicit val arbitraryMoneyEUR: Arbitrary[MoneyY[CurrencyY.EUR.type]] = fArbitraryMoney(EUR)
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

}
