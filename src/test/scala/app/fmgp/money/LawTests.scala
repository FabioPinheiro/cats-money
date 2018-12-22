package app.fmgp.money


import cats.kernel.laws.discipline._
import cats.tests.CatsSuite
import org.scalacheck.{Arbitrary, Cogen, Gen}

/** testOnly app.fmgp.money.MoneyXLawTests */
class MoneyXLawTests extends CatsSuite {
  import app.fmgp.money.Currency.CurrencyX //The currency used on the tests

  implicit def arbitraryCurrency: Arbitrary[CurrencyX] = Arbitrary(Gen.oneOf(Currency.values.toSeq))
  implicit val cogenCurrencyX: Cogen[CurrencyX] = Cogen[String].contramap(_.toString)

  implicit def arbitraryMoneyX: Arbitrary[MoneyX[CurrencyX]] = Arbitrary(for {
    amount <- Gen.chooseNum(-1000d, 1000d).map(BigDecimal.apply)
    currency <- arbitraryCurrency.arbitrary
  } yield MoneyX[CurrencyX](amount, currency))
  implicit def cogenMoneyX: Cogen[MoneyX[CurrencyX]] = Cogen[String].contramap(_.toString)
  //implicit val wtfTODO = Arbitrary((x: MoneyX[CurrencyX]) => x) //QUESTION SOLVED


  //import Currency.eqv
  import MoneyX._
  import Wallet.X._

  implicit def arbitraryMoneyXMap1: Arbitrary[Wallet[CurrencyX]] = Arbitrary(for {
    money <- Gen.atLeastOne(arbitraryMoneyX.arbitrary, arbitraryMoneyX.arbitrary)
    moneyX = money.map(e => Wallet.X.fromMoney[CurrencyX](e))
  } yield moneyXCommutativeGroup.combineAll(moneyX))
  implicit def cogenMoneyXMap: Cogen[Wallet[CurrencyX]] = Cogen[String].contramap(_.w.toVector.sortBy(_._1).toString)



  checkAll("Currency", EqTests[CurrencyX].eqv)
  checkAll("MoneyX", EqTests[MoneyX[CurrencyX]].eqv)
  //import MoneyXMonoid._ //MoneyX can't be a Monoid!
  //checkAll("MoneyX", MonoidTests[MoneyX[CurrencyX]].monoid) //THIS IS NO GOOD
  checkAll("Wallet", EqTests[Wallet[CurrencyX]].eqv)
  checkAll("Wallet", CommutativeSemigroupTests[Wallet[CurrencyX]].commutativeSemigroup)
}


/** testOnly app.fmgp.money.MoneyYLawTests */
class MoneyYLawTests extends CatsSuite {
  import app.fmgp.money.CurrencyY._ //The currency used on the tests

  implicit def arbitraryCurrency: Arbitrary[CY] = Arbitrary(Gen.oneOf(CYValues))
  implicit val cogenCurrencyX: Cogen[CY] = Cogen[String].contramap(_.toString)

  implicit def arbitraryMoneyX: Arbitrary[MoneyY[CY]] = Arbitrary(for {
    amount <- Gen.chooseNum(-1000d, 1000d).map(BigDecimal.apply)
    currency <- arbitraryCurrency.arbitrary
  } yield MoneyY[CY](amount, currency))
  implicit def cogenMoneyX: Cogen[MoneyY[CY]] = Cogen[String].contramap(_.toString)
  //implicit val wtfTODO = Arbitrary((x: MoneyX[CurrencyX]) => x) //QUESTION SOLVED

  implicit def arbitraryMoneyXXX: Arbitrary[MoneyY[XXX.type]] = Arbitrary(for {
    amount <- Gen.chooseNum(-1000d, 1000d).map(BigDecimal.apply)
  } yield MoneyY[XXX.type](amount, XXX))

  //import Currency.eqv
  import MoneyY._
  import MoneyYMonoid._
//  import MoneyYMapCommutativeGroup._
//
//  implicit def arbitraryMoneyXMap1: Arbitrary[MoneyXMap[CurrencyX]] = Arbitrary(for {
//    money <- Gen.atLeastOne(arbitraryMoneyX.arbitrary, arbitraryMoneyX.arbitrary)
//    moneyX = money.map(e => MoneyXMap.fromMoney[CurrencyX](e))
//  } yield moneyXCommutativeGroup.combineAll(moneyX))
//  implicit def cogenMoneyXMap: Cogen[MoneyXMap[CurrencyX]] = Cogen[String].contramap(_.w.toVector.sortBy(_._1).toString)



  checkAll("Currency", EqTests[CY].eqv)
  checkAll("MoneyY", EqTests[MoneyY[CY]].eqv)
  import app.fmgp.money.MoneyYMonoid._
  implicit val qwertyuiop = fMoneyYMonoid(XXX)
  checkAll("MoneyY(XXX)", MonoidTests[MoneyY[XXX.type]].monoid)
  //checkAll("MoneyYMap", EqTests[MoneyYMap[CY]].eqv)
  //checkAll("MoneyYMap", CommutativeSemigroupTests[MoneyYMap[CY]].commutativeSemigroup)
}
