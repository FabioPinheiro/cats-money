package app.fmgp.money
import app.fmgp.typeclasses.{_, given _}

object instances {
  given moneyTreeFunctor as Functor[MoneyTree] {
    def [A, B](original: MoneyTree[A]).map(mapper: A => B): MoneyTree[B] = original match {
      case MoneyBranch(value) => MoneyBranch(value.map(e => map(e)(mapper)))
      case MoneyLeaf(value)   => MoneyLeaf[B](mapper(value))
    }
  }
  given moneyTreeMonad as Monad[MoneyTree] {
    def pure[A](x: A): MoneyTree[A] = MoneyLeaf(x)
    def [A, B](xs: MoneyTree[A]).flatMap(f: A => MoneyTree[B]): MoneyTree[B] =
      xs match {
      case MoneyBranch(v: Seq[MoneyTree[A]]) => MoneyBranch[B](v.map(flatMap(_)(f)))
      case MoneyLeaf(v)                      => f(v)
    }
  }
}
