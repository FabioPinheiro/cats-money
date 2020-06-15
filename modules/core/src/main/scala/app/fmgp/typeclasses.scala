package app.fmgp


/**
 * This typeclasses implementaion is base on the one sugested by the dotty documentaion. 
 * @see [[https://dotty.epfl.ch/docs/reference/contextual/type-classes.html]]
 * 
 *
 * {{{ import app.fmgp.typeclasses.{_, given _} }}}
 */
object typeclasses {
  /** 
   * When you have two or more given for `Monad[F]`
   * you may want to call .pure like:
   * {{{
   *   "".pure[String, List]
   *   "".pure: List[String]
   *   "".pure(using summon[Monad[List]])
   *   val a: List[String] = "".pure
   * }}}
   * In a post-3.0 future we will have multiple type parameter lists.
   * This way pure call like {{{"".pure[List]}}}
   */
  def [A, F[_]](x: A).pure(using m: Monad[F]): F[A] = m.pure(x)
  
  trait SemiGroup[T] {
    def (x: T).combine(y: T): T
  }

  trait Monoid[T] extends SemiGroup[T] {
    def unit: T
  }

  object Monoid {
    def apply[T](using m: Monoid[T]) = m
    def [T: Monoid](xs: List[T]).combineAll: T = xs.foldLeft(Monoid[T].unit)(_ combine _)  
  }

  trait Functor[F[_]] {
    def [A, B](original: F[A]).map(mapper: A => B): F[B]
  }

  object Functor {
    given Functor[List] {
      def [A, B](original: List[A]).map(mapper: A => B): List[B] = original.map(mapper)
    }
  }

  trait Monad[F[_]] extends Functor[F] { // "A `Monad` for type `F[?]` is a `Functor[F]`" => thus has the `map` ability
    def pure[A](x: A): F[A]
    def [A, B](x: F[A]).flatMap(f: A => F[B]): F[B]
    def [A, B](x: F[A]).map(f: A => B) = x.flatMap(f `andThen` pure)
  }

  object Monad {
    given listMonad as Monad[List] {
      def pure[A](x: A): List[A] = List(x)
      def [A, B](xs: List[A]).flatMap(f: A => List[B]): List[B] = xs.flatMap(f)
    }

    given optionMonad as Monad[Option] {
      def pure[A](x: A): Option[A] = Option(x)
      def [A, B](xs: Option[A]).flatMap(f: A => Option[B]): Option[B] = xs.flatMap(f)
    }
  }

  // /** @see [[http://eed3si9n.com/learning-scalaz/Id.html]] */
  // type Id[+X] = X

  // /** @see [[http://eed3si9n.com/herding-cats/Writer.html]] */
  // type Writer[L, V] = WriterT[Id, L, V]
  // object Writer {
  //   def apply[L, V](l: L, v: V): WriterT[Id, L, V] = WriterT[Id, L, V]((l, v))
  //   // def value[L:Monoid, V](v: V): Writer[L, V] = WriterT((List.empty, v))
  //   def tell[L](l: L): Writer[L, Unit] = WriterT((l, ()))
  // }

  // final case class WriterT[F[_], L, V]( run: F[(L, V)]) {

  //   def tell(l: L)(using functorF: Functor[F], semigroupL: SemiGroup[L]): WriterT[F, L, V] = mapWritten(f => f.combine(l))

  //   def written(using functorF: Functor[F]): F[L] = functorF.map(run)(_._1)

  //   def value(using functorF: Functor[F]): F[V] = functorF.map(run)(_._2)

  //   def mapBoth[M, U](f: (L, V) => (M, U))(using functorF: Functor[F]): WriterT[F, M, U] = WriterT { functorF.map(run)(f.tupled) }

  //   def mapWritten[M](f: L => M)(using functorF: Functor[F]): WriterT[F, M, V] = mapBoth((l, v) => (f(l), v))
  // }
}
