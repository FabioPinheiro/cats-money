package app.fmgp.money
import app.fmgp.typeclasses.{_, given}

object instances {
  given moneyTreeFunctor: Functor[MoneyTree] with
    extension [A, B](original: MoneyTree[A])
      def map(mapper: A => B): MoneyTree[B] = original match {
        //FIXME case MoneyBranch(value) => MoneyBranch(value.map(e => map(e)(mapper)))
        case MoneyBranch(value) => MoneyBranch(value.map(e => map(mapper)))
        case MoneyLeaf(value)   => MoneyLeaf[B](mapper(value))
      }

  given moneyTreeMonad: Monad[MoneyTree] with
    def pure[A](x: A): MoneyTree[A] = MoneyLeaf(x)
    
    extension [A, B](xs: MoneyTree[A])
      def flatMap(f: A => MoneyTree[B]): MoneyTree[B] =
        xs match {
          //FIXME case MoneyBranch(v: Seq[MoneyTree[A]]) => MoneyBranch[B](v.map(flatMap(_)(f)))
          case MoneyBranch(v: Seq[MoneyTree[A]]) => MoneyBranch[B](v.map(e => flatMap(f)))
          case MoneyLeaf(v)                      => f(v)
        }
}
