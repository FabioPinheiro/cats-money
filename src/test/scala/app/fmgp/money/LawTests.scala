package app.fmgp.money

import app.fmgp.money.CurrencyY._
import app.fmgp.money.MoneyTree._
import app.fmgp.money.instances.all._
import cats.kernel.laws.discipline._
import cats.laws.discipline.{FunctorTests, MonadTests}
import cats.tests.CatsSuite

/** testOnly app.fmgp.money.MoneyYLawTests */
class MoneyYLawTests extends CatsSuite with MoneyTestUtils {



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
  checkAll("MoneyTree", MonadTests[MoneyTree].monad[MoneyY[CY], MoneyY[EUR.type], MoneyY[XXX.type]])
}
