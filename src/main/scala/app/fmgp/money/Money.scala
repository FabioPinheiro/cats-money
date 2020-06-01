package app.fmgp.money

sealed abstract case class Money[+T](amount: BigDecimal, currency: T) derives Eql

object Money {
  def apply[T](amount: BigDecimal, t: T): Money[T] = new Money[T](amount, t) {}
  def fromTuple[T](m: (T, BigDecimal)): Money[T] = Money.apply(m._2, m._1)
}