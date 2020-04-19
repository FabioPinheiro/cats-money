package app.fmgp.money

trait Companion[T] {
  type C
  def apply(): C
}

object Companion {
  //FIXME DOTTY implicit def companion[T](implicit comp: Companion[T]) = comp()
  def of[T, F[_]](a: F[T])(implicit comp: Companion[T]) = comp.apply()
  //implicit class MoneyWithCompanion[T](value: MoneyZ[T]) {
  //  def currency(implicit companion: Companion[T]) = companion.apply()
  //} //No need from this
}
