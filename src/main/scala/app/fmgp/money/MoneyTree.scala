package app.fmgp.money

import cats.Monad
import cats.data.Writer
import cats.kernel.{Eq, Monoid}

sealed abstract class MoneyTree[+T] {
  override def toString: String = s"MoneyTree(${collectValues.mkString(", ")}})"
  def collapse: MoneyTree[T]
  def collectValues: Seq[T]
  def simplify[S >: T](implicit monoid: Monoid[S]): MoneyTree[S] =
    MoneyTree.leafs[S](monoid.combineAll(this.collectValues: TraversableOnce[T]))

  def :::[S >: T](other: MoneyTree[S]): MoneyTree[S]
  def ::[S >: T](a: S): MoneyTree[S] = prepend(a)
  def +:[S >: T](a: S): MoneyTree[S] = prepend(a)
  def :+[S >: T](a: S): MoneyTree[S] = append(a)
  def ++[S >: T](l: Seq[S]): MoneyTree[S] = concat(l)
  def prepend[S >: T](a: S): MoneyTree[S]
  def append[S >: T](a: S): MoneyTree[S]
  def concat[S >: T](other: Seq[S]): MoneyTree[S]

  def convert[TT](converter: Converter[T, TT])(implicit mt: Monad[MoneyTree]): MoneyTree[TT] =
    mt.map(this)(e => converter.convert(e))
  def convertWithLog[TT](
      converter: Converter[T, TT]
  )(implicit mt: Monad[MoneyTree]): MoneyTree[Writer[Vector[String], TT]] =
    mt.map(this)(e => converter.convertWithLog(e))
  def total[S >: T](implicit monoid: Monoid[S]): S
}

final case class MoneyZBranch[T](value: Seq[MoneyTree[T]]) extends MoneyTree[T] {
  override def collectValues: Seq[T] = value.map(_.collapse).flatMap {
    case MoneyZBranch(v: Seq[MoneyTree[T]]) => v.flatMap(_.collectValues)
    case o @ MoneyZLeaf(_)                  => o.collectValues //Seq(v)
  }
  override def collapse: MoneyZBranch[T] = MoneyZBranch[T](value.flatMap(_.collectValues).map(MoneyZLeaf.apply))
  override def prepend[S >: T](a: S): MoneyTree[S] = MoneyTree.branch(MoneyTree.one[S](a) +: value)
  override def append[S >: T](a: S): MoneyTree[S] = MoneyTree.branch(value :+ MoneyTree.one[S](a))
  override def :::[S >: T](other: MoneyTree[S]): MoneyTree[S] = other match {
    case MoneyZBranch(v)  => MoneyZBranch(value ++ v)
    case v: MoneyZLeaf[S] => MoneyZBranch(value :+ v)
  }
  def concat[S >: T](other: Seq[S]): MoneyTree[S] = MoneyZBranch(value ++ other.map(MoneyTree.one))
  override def total[S >: T](implicit monoid: Monoid[S]): S = monoid.combineAll(value.map(_.total(monoid)))
}

final case class MoneyZLeaf[T](value: T) extends MoneyTree[T] {
  override def collectValues: Seq[T] = Seq(value)
  override def collapse: MoneyZLeaf[T] = this
  override def prepend[S >: T](a: S): MoneyTree[S] = MoneyTree.leafs(a, value)
  override def append[S >: T](a: S): MoneyTree[S] = MoneyTree.leafs(value, a)
  override def :::[S >: T](other: MoneyTree[S]): MoneyTree[S] = other match {
    case MoneyZBranch(v)  => MoneyZBranch(this +: v)
    case v: MoneyZLeaf[S] => MoneyZBranch[S](Seq(this, v))
  }
  def concat[S >: T](other: Seq[S]): MoneyTree[S] = MoneyZBranch((value +: other).map(MoneyTree.one))
  override def total[S >: T](implicit monoid: Monoid[S]): S = value
}

object MoneyTree {
  def empty[T]: MoneyTree[T] = MoneyZBranch[T](Seq.empty)
  def one[T](value: T): MoneyTree[T] = MoneyZLeaf[T](value)
  def branch[T](m: Seq[MoneyTree[T]]): MoneyTree[T] = MoneyZBranch[T](m)
  def join[T](m: MoneyTree[T]*): MoneyTree[T] = MoneyZBranch[T](m)
  def leafs[T](m: T*): MoneyTree[T] = MoneyZBranch[T](m.map(one))

  implicit def eqTree[T: Eq]: Eq[MoneyTree[T]] = Eq.fromUniversalEquals
}
