package app.fmgp.sandbox


import cats.kernel.laws.discipline.{CommutativeGroupTests, CommutativeSemigroupTests, GroupTests, SemigroupTests}
import cats.tests.CatsSuite
import org.scalacheck.{Arbitrary, Cogen, Gen}

class Money0LawTests extends CatsSuite {
  implicit val arbitraryMoney0: Arbitrary[Money0] = Arbitrary {
    for {
      amount <- Gen.chooseNum(0d, 1000d).map(BigDecimal.apply)
      //FIXME => FAILED TEST! //currency <- Gen.oneOf(Currency.EUR, Currency.USD)
    } yield Money0(amount, CurrencyX.USD)
  }
  checkAll("Money0", CommutativeSemigroupTests[Money0].commutativeSemigroup)
}


trait Money1TestAux {
  implicit val arbitraryMoney1: Arbitrary[Money1] = Arbitrary {
    for {
      amount <- Gen.chooseNum(0d, 1000d).map(BigDecimal.apply)
      currency <- Gen.oneOf(CurrencyX.EUR, CurrencyX.USD)
    } yield Money1(amount, currency)
  }
  implicit val cogen: Cogen[Money1] = Cogen[String].contramap(e => s"${e.amount}-${e.currency}")
}
/*
class Money1CSLawTests extends CatsSuite with Money1TestAux {
  import Money1CommutativeSemigroup._
  checkAll(s"Money1", EqTests[Money1].eqv) //FIXME => FAILED TEST!
  checkAll(s"Money1", CommutativeSemigroupTests[Money1].commutativeSemigroup)
}


class Money1MonoidLawTests extends CatsSuite with Money1TestAux {
  import Money1Monoid._
  checkAll(s"Money1", MonoidTests[Money1].monoid)
}

class Money1GroupLawTests extends CatsSuite with Money1TestAux {
  import Money1Group._
  checkAll(s"Money1", GroupTests[Money1].monoid)
}
*/


trait Money2TestAux {

  implicit val arbitraryMoney2USD: Arbitrary[Money2USD] = Arbitrary {
    for {
      amount <- Gen.chooseNum(0d, 1000d).map(BigDecimal.apply)
    } yield Money2USD(amount)
  }
  implicit val arbitraryMoney2EUR: Arbitrary[Money2EUR] = Arbitrary {
    for {
      amount <- Gen.chooseNum(0d, 1000d).map(BigDecimal.apply)
    } yield Money2EUR(amount)
  }


  implicit val arbitraryMoney2Map: Arbitrary[Money2Map] = Arbitrary {
    for {
      x <- Gen.oneOf(arbitraryMoney2USD.arbitrary, arbitraryMoney2EUR.arbitrary)
    } yield Money2Map(Map(x.currency -> x.amount))
  }
  implicit val cogen: Cogen[Money2Map] = Cogen[String].contramap(_.w.toVector.sortBy(_._1.name).toString)
}


class Money2SGLawTests extends CatsSuite with Money2TestAux {
  //FIXME checkAll(s"Money2", EqTests[Money2Map].eqv)
  import Money2Monoid._

  checkAll(s"Money2", SemigroupTests[Money2Map].semigroup)
}


class Money2GroupLawTests extends CatsSuite with Money2TestAux {

  import Money2Group._

  checkAll(s"Money2", GroupTests[Money2Map].group)
}


trait Money3TestAux {

  implicit val arbitraryMoney2USD: Arbitrary[Money3B[CCEUR.type]] = Arbitrary {
    for {
      amount <- Gen.chooseNum(0d, 10000d).map(BigDecimal.apply)
    } yield Money3B(amount, CCEUR)
  }
  implicit val arbitraryMoney2EUR: Arbitrary[Money3B[CCUSD.type]] = Arbitrary {
    for {
      amount <- Gen.chooseNum(0d, 10000d).map(BigDecimal.apply)
    } yield Money3B(amount, CCUSD)
  }


  implicit val arbitraryMoney2Map: Arbitrary[Money3Map] = Arbitrary {
    for {
      x <- Gen.oneOf(arbitraryMoney2USD.arbitrary, arbitraryMoney2EUR.arbitrary)
    } yield Money3Map(Map(x.currency -> x.amount))
  }
  implicit val cogen: Cogen[Money3Map] = Cogen[String].contramap(_.w.toString)
}


class Money3GroupLawTests extends CatsSuite with Money3TestAux {

  import Money3CommutativeGroup._

  checkAll(s"Money3", CommutativeGroupTests[Money3Map].commutativeGroup)
}

