package app.fmgp.sandbox

import app.fmgp.sandbox.CurrencyX._
import app.fmgp.sandbox.MoneyX._
import app.fmgp.sandbox.Wallet.X._
import cats.kernel.laws.discipline._
import cats.tests.CatsSuite
import org.scalacheck.{Arbitrary, Cogen, Gen}

/** testOnly app.fmgp.sandbox.MoneyXLawTests */
class MoneyXLawTests extends CatsSuite {

  // Currency

  implicit def arbitraryCurrency: Arbitrary[CurrencyX] = Arbitrary(Gen.oneOf(CurrencyX.values.toSeq.filterNot(_ == XTS)))
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
  implicit def cogenMoneyXMap: Cogen[Wallet[CurrencyX]] = Cogen[String].contramap { wallet =>
    val vector = wallet.w.toVector.sortBy(_._1)
    vector.toString
  }


  checkAll("Currency", EqTests[CurrencyX].eqv)
  checkAll("MoneyX", EqTests[MoneyX[CurrencyX]].eqv)
  //import MoneyXMonoid._ //MoneyX can't be a Monoid!
  //checkAll("MoneyX", MonoidTests[MoneyX[CurrencyX]].monoid) //THIS IS NO GOOD
  checkAll("Wallet", EqTests[Wallet[CurrencyX]].eqv)
  checkAll("Wallet", CommutativeSemigroupTests[Wallet[CurrencyX]].commutativeSemigroup)
}
