package app.fmgp.money

// import cats.Monad
// import cats.data.Writer
// import cats.kernel.{Eq, Monoid, Semigroup}

sealed abstract class MoneyTree[+T] {
  override def toString: String = s"MoneyTree(${collectValues.mkString(", ")})"
  def collapse: MoneyTree[T]
  def collectValues: Seq[T]
  // def simplify[S >: T](implicit monoid: Monoid[S]): MoneyTree[S] =
  //   MoneyTree.leafs[S](monoid.combineAll(this.collectValues: TraversableOnce[T]))

  // def simplifyWIP[S](implicit semigroup: Semigroup[Money[S]]): MoneyTree[Money[S]] = {
  //   collectValues
  //   def aaaa(seq: Seq[Money[S]]) = seq match {
  //     case m +: Seq() => Seq(m)
  //     case m +: tail  => tail.collect { case o if o.currency == m.currency => o }
  //   }
  //   ???
  // }

  def :::[S >: T](other: MoneyTree[S]): MoneyTree[S]
  def ::[S >: T](a: S): MoneyTree[S] = prepend(a)
  def +:[S >: T](a: S): MoneyTree[S] = prepend(a)
  def :+[S >: T](a: S): MoneyTree[S] = append(a)
  def ++[S >: T](l: Seq[S]): MoneyTree[S] = concat(l)
  def prepend[S >: T](a: S): MoneyTree[S]
  def append[S >: T](a: S): MoneyTree[S]
  def concat[S >: T](other: Seq[S]): MoneyTree[S]

  // def convert[TT](converter: Converter[T, TT])(implicit mt: Monad[MoneyTree]): MoneyTree[TT] =
  //   mt.map(this)(e => converter.convert(e))
  // def convertWithLog[TT](
  //     converter: Converter[T, TT]
  // )(implicit mt: Monad[MoneyTree]): MoneyTree[Writer[Vector[String], TT]] =
  //   mt.map(this)(e => converter.convertWithLog(e))
  // def total[S >: T](implicit monoid: Monoid[S]): S
}

final case class MoneyBranch[T](value: Seq[MoneyTree[T]]) extends MoneyTree[T] {
  override def collectValues: Seq[T] = value.map(_.collapse).flatMap {
    case MoneyBranch(v: Seq[MoneyTree[T]]) => v.flatMap(_.collectValues)
    case o @ MoneyLeaf(_)                  => o.collectValues //Seq(v)
  }
  override def collapse: MoneyBranch[T] = MoneyBranch[T](value.flatMap(_.collectValues).map(MoneyLeaf.apply))
  override def prepend[S >: T](a: S): MoneyTree[S] = MoneyTree.branch(MoneyTree.one[S](a) +: value)
  override def append[S >: T](a: S): MoneyTree[S] = MoneyTree.branch(value :+ MoneyTree.one[S](a))
  override def :::[S >: T](other: MoneyTree[S]): MoneyTree[S] = other match {
    case MoneyBranch(v)   => MoneyBranch(value ++ v)
    case v @ MoneyLeaf(_) => MoneyBranch(value :+ v)
  }
  def concat[S >: T](other: Seq[S]): MoneyTree[S] = MoneyBranch(value ++ other.map(MoneyTree.one))
  // override def total[S >: T](implicit monoid: Monoid[S]): S = monoid.combineAll(value.map(_.total(monoid)))
}

final case class MoneyLeaf[T](value: T) extends MoneyTree[T] {
  override def collectValues: Seq[T] = Seq(value)
  override def collapse: MoneyLeaf[T] = this
  override def prepend[S >: T](a: S): MoneyTree[S] = MoneyTree.leafs(a, value)
  override def append[S >: T](a: S): MoneyTree[S] = MoneyTree.leafs(value, a)
  override def :::[S >: T](other: MoneyTree[S]): MoneyTree[S] = other match {
    case MoneyBranch(v)   => MoneyBranch(this +: v)
    case v @ MoneyLeaf(_) => MoneyBranch[S](Seq(this, v))
  }
  def concat[S >: T](other: Seq[S]): MoneyTree[S] = MoneyBranch((value +: other).map(MoneyTree.one))
  // override def total[S >: T](implicit monoid: Monoid[S]): S = value
}

object MoneyTree {
  def empty[T]: MoneyTree[T] = MoneyBranch[T](Seq.empty)
  def one[T](value: T): MoneyTree[T] = MoneyLeaf[T](value)
  def branch[T](m: Seq[MoneyTree[T]]): MoneyTree[T] = MoneyBranch[T](m)
  def join[T](m: MoneyTree[T]*): MoneyTree[T] = MoneyBranch[T](m)
  def leafs[T](m: T*): MoneyTree[T] = MoneyBranch[T](m.map(one))

  // implicit def eqTree[T: Eq]: Eq[MoneyTree[T]] = Eq.fromUniversalEquals
}
