package app.fmgp.money

trait Companion[T] {
  type C
  def apply(): C
}

object Companion {
  //FIXME DOTTY implicit def companion[T](implicit comp: Companion[T]) = comp()

  //def of[T, F[_]](a: F[T])(using comp: Companion[T]) = comp.apply()
}
