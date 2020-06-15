package app.fmgp.money

import app.fmgp.money.Currency._
import app.fmgp.money.MoneyTree._
import app.fmgp.money.instances.all._
import cats.kernel.laws.discipline._
import cats.laws.discipline.{FunctorTests, MonadTests, MonoidKTests, TraverseTests}
import cats.tests.CatsSuite

/** testOnly app.fmgp.money.MoneyLawTests */
class MoneyLawTests extends CatsSuite with MoneyTestUtils {

  //import Currency.eqv
  //  import MoneyMapCommutativeGroup._
  //
  //  implicit def arbitraryMoneyXMap1: Arbitrary[MoneyXMap[CurrencyX]] = Arbitrary(for {
  //    money <- Gen.atLeastOne(arbitraryMoneyX.arbitrary, arbitraryMoneyX.arbitrary)
  //    moneyX = money.map(e => MoneyXMap.fromMoney[CurrencyX](e))
  //  } yield moneyXCommutativeGroup.combineAll(moneyX))
  //  implicit def cogenMoneyXMap: Cogen[MoneyXMap[CurrencyX]] = Cogen[String].contramap(_.w.toVector.sortBy(_._1).toString)

  checkAll("Currency", EqTests[CY].eqv)
  checkAll("Money", EqTests[Money[CY]].eqv)

  {
    implicit val moneyMonoidXXX = app.fmgp.money.instances.money.MoneyMonoidT(XXX)
    checkAll("Money(XXX)", MonoidTests[Money[XXX.type]].monoid)
  }

  //checkAll("MoneyMap", EqTests[MoneyMap[CY]].eqv)
  //checkAll("MoneyMap", CommutativeSemigroupTests[MoneyMap[CY]].commutativeSemigroup)
  checkAll("MoneyK", MonoidKTests[MoneyK].monoidK[CY])

  checkAll("MoneyTree", FunctorTests[MoneyTree].functor[Money[CY], Money[EUR.type], Money[XXX.type]])
  checkAll("MoneyTree", MonadTests[MoneyTree].monad[Money[CY], Money[EUR.type], Money[XXX.type]])

  //FIXME
  //checkAll("MoneyTreeTraverse", TraverseTests[MoneyTree](MoneyTreeTraverse).functor[Money[CY], Money[EUR.type], Money[XXX.type]])

}
