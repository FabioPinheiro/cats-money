package app.fmgp.sandbox

import cats.kernel.{CommutativeSemigroup, Eq, Group, Monoid}


case class Money1(amount: BigDecimal, currency: CurrencyX.Value)

object Money1 {
  implicit val eq: Eq[Money1] = Eq.instance((a, b) => a.amount == b.amount) //FIXME
}


object Money1CommutativeSemigroup {
  implicit val commutativeSemigroup: CommutativeSemigroup[Money1] = new CommutativeSemigroup[Money1] {
    override def combine(x: Money1, y: Money1): Money1 = Money1(x.amount + y.amount, CurrencyX.XTS)
  }
}


object Money1Monoid {
  implicit val monoid: Monoid[Money1] = new Monoid[Money1] {
    override def combine(x: Money1, y: Money1): Money1 = Money1(x.amount + y.amount, CurrencyX.XTS)
    override def empty: Money1 = Money1(0, CurrencyX.XTS)
  }
}

object Money1Group {
  implicit val monoid: Group[Money1] = new Group[Money1] {
    override def combine(x: Money1, y: Money1): Money1 = Money1(x.amount + y.amount, CurrencyX.XTS)
    override def empty: Money1 = Money1(0, CurrencyX.XTS)
    override def inverse(a: Money1): Money1 = Money1(a.amount, CurrencyX.XTS)
  }
}

