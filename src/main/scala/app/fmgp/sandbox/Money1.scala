package app.fmgp.sandbox

//import cats.Order
import app.fmgp.money.Currency
import cats.kernel.{CommutativeSemigroup, Eq, Group, Monoid}


case class Money1(amount: BigDecimal, currency: Currency.Value)

object Money1{
  implicit val eq: Eq[Money1] = Eq.instance((a,b) => a.amount == b.amount) //FIXME
}


object Money1CommutativeSemigroup {
  implicit val commutativeSemigroup: CommutativeSemigroup[Money1] = new CommutativeSemigroup[Money1] {
    override def combine(x: Money1, y: Money1): Money1 = Money1(x.amount + y.amount, Currency.XTS)
  }
}


object Money1Monoid {
  implicit val monoid: Monoid[Money1] = new Monoid[Money1] {
    override def combine(x: Money1, y: Money1): Money1 = Money1(x.amount + y.amount, Currency.XTS)
    override def empty: Money1 = Money1(0, Currency.XTS)
  }
}

object Money1Group {
  implicit val monoid: Group[Money1] = new Group[Money1] {
    override def combine(x: Money1, y: Money1): Money1 = Money1(x.amount + y.amount, Currency.XTS)
    override def empty: Money1 = Money1(0, Currency.XTS)
    override def inverse(a: Money1): Money1 = Money1(a.amount, Currency.XTS)
  }
}

