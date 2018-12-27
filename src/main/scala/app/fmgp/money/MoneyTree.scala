package app.fmgp.money


import cats.kernel.{Eq, Monoid}

sealed abstract class MoneyTree[+T] {
  def collapse: MoneyTree[T]
  def collectValues: Seq[T]
  def simplify[S >: T](implicit monoid: Monoid[S]): MoneyTree[S] =
    MoneyTree.joinLeaf[S](monoid.combineAll(this.collectValues:TraversableOnce[T]))
}
final case class MoneyZBranch[T](value: Seq[MoneyTree[T]]) extends MoneyTree[T] {
  override def collectValues: Seq[T] = value.map(_.collapse).flatMap {
    case MoneyZBranch(v: Seq[MoneyTree[T]]) => v.flatMap(_.collectValues)
    case o@MoneyZLeaf(_) => o.collectValues //Seq(v)
  }
  override def collapse: MoneyZBranch[T] = MoneyZBranch[T](value.flatMap(_.collectValues).map(MoneyZLeaf.apply))
}
final case class MoneyZLeaf[T](value: T) extends MoneyTree[T] {
  override def collectValues: Seq[T] = Seq(value)
  override def collapse: MoneyZLeaf[T] = this
}


object MoneyTree {
  def empty[T]: MoneyTree[T] = MoneyZBranch[T](Seq.empty)
  def branch[T](m: Seq[MoneyTree[T]]): MoneyTree[T] = MoneyZBranch[T](m)
  def join[T](m: MoneyTree[T]*): MoneyTree[T] = MoneyZBranch[T](m)
  def joinLeaf[T](m: T*): MoneyTree[T] = MoneyZBranch[T](m.map(leaf))
  def leaf[T](value: T): MoneyTree[T] = MoneyZLeaf[T](value)

  implicit def eqTree[T: Eq]: Eq[MoneyTree[T]] = Eq.fromUniversalEquals
}
