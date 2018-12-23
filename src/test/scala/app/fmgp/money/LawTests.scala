package app.fmgp.money


import cats.kernel.laws.discipline._
import cats.laws.discipline.FunctorTests
import cats.tests.CatsSuite
import org.scalacheck.{Arbitrary, Cogen, Gen}

/** testOnly app.fmgp.money.MoneyXLawTests */
class MoneyXLawTests extends CatsSuite {

  import app.fmgp.money.Currency._ //The currency used on the tests
  import app.fmgp.money.MoneyX._
  import app.fmgp.money.Wallet.X._

  // Currency

  implicit def arbitraryCurrency: Arbitrary[CurrencyX] = Arbitrary(Gen.oneOf(Currency.values.toSeq.filterNot(_ == XTS)))
  implicit val cogenCurrencyX: Cogen[CurrencyX] = Cogen[String].contramap(_.toString)

  // MoneyY

  implicit def arbitraryMoneyX: Arbitrary[MoneyX[CurrencyX]] = Arbitrary(for {
    amount <- Gen.chooseNum(-1000d, 1000d, 0).map(BigDecimal.apply)
    currency <- arbitraryCurrency.arbitrary
  } yield MoneyX[CurrencyX](amount, currency))
  implicit def cogenMoneyX: Cogen[MoneyX[CurrencyX]] = Cogen[String].contramap(_.toString)

  // Wallet

  implicit def arbitraryMoneyXWallet: Arbitrary[Wallet[CurrencyX]] = Arbitrary(g = for {
    currency <- arbitraryCurrency.arbitrary
    //money <- Gen.atLeastOne(MoneyX(0,currency), arbitraryMoneyX.arbitrary)
    walletF = arbitraryMoneyX.arbitrary.map(e => Wallet.X.fromMoney[CurrencyX](e))
    wallets <- Gen.listOfN(5, walletF)
    wallet = moneyXCommutativeGroup.combineAll(wallets ++ Seq(
      Wallet.X.fromMoney(MoneyX(10.0, XTS)),
      Wallet.X.fromMoney(MoneyX(-10.0, XTS))
    ))
  } yield wallet)
  implicit def cogenMoneyXMap: Cogen[Wallet[CurrencyX]] = Cogen[String].contramap(_.w.toVector.sortBy(_._1).toString)


  checkAll("Currency", EqTests[CurrencyX].eqv)
  checkAll("MoneyX", EqTests[MoneyX[CurrencyX]].eqv)
  //import MoneyXMonoid._ //MoneyX can't be a Monoid!
  //checkAll("MoneyX", MonoidTests[MoneyX[CurrencyX]].monoid) //THIS IS NO GOOD
  checkAll("Wallet", EqTests[Wallet[CurrencyX]].eqv)
  checkAll("Wallet", CommutativeSemigroupTests[Wallet[CurrencyX]].commutativeSemigroup)
}


/** testOnly app.fmgp.money.MoneyYLawTests */
class MoneyYLawTests extends CatsSuite with MoneyTestUtils {

  import app.fmgp.money.CurrencyY._ //The currency used on the tests
  import app.fmgp.money.MoneyYMonoid._
  import app.fmgp.money.MoneyY._
  import app.fmgp.money.MoneyYMonoid._
  import app.fmgp.money.MoneyTree._

  //import Currency.eqv
  //  import MoneyYMapCommutativeGroup._
  //
  //  implicit def arbitraryMoneyXMap1: Arbitrary[MoneyXMap[CurrencyX]] = Arbitrary(for {
  //    money <- Gen.atLeastOne(arbitraryMoneyX.arbitrary, arbitraryMoneyX.arbitrary)
  //    moneyX = money.map(e => MoneyXMap.fromMoney[CurrencyX](e))
  //  } yield moneyXCommutativeGroup.combineAll(moneyX))
  //  implicit def cogenMoneyXMap: Cogen[MoneyXMap[CurrencyX]] = Cogen[String].contramap(_.w.toVector.sortBy(_._1).toString)


  checkAll("Currency", EqTests[CY].eqv)
  checkAll("MoneyY", EqTests[MoneyY[CY]].eqv)
  checkAll("MoneyY(XXX)", MonoidTests[MoneyY[XXX.type]].monoid)
  //checkAll("MoneyYMap", EqTests[MoneyYMap[CY]].eqv)
  //checkAll("MoneyYMap", CommutativeSemigroupTests[MoneyYMap[CY]].commutativeSemigroup)

  checkAll("MoneyTree", FunctorTests[MoneyTree].functor[MoneyY[CY], MoneyY[EUR.type], MoneyY[XXX.type]])

}
