package app.fmgp.money

object Money {
  def apply[T](amount: MoneyAmount, t: T): Money[T] = new Money[T](amount, t) {}
  def fromTuple[T](m: (T, MoneyAmount)): Money[T] = Money.apply(m._2, m._1)
}

sealed abstract case class Money[T](amount: MoneyAmount, currency: T) derives Eql {
  //extends scala.math.ScalaNumber with ScalaNumericConversions with Serializable with Ordered[MoneyAmount] // Existes a Eql[Number,Number] ...
  // override def doubleValue(): Double = amount.doubleValue
  // override def floatValue(): Float = amount.floatValue
  // override def intValue(): Int = amount.intValue
  // override def longValue(): Long = amount.longValue
  // // Members declared in scala.math.ScalaNumber
  // override def isWhole(): Boolean = amount.isWhole
  // override def underlying(): Object = amount.underlying

  /** Addition of Money - Must be on the safe type of currency */
  def +[C] (that: Money[C])(using =:=[C,T]): Money[T] = Money[T](this.amount + that.amount, this.currency)
  //def +(that: Money[T]): Money[T] = Money[T](this.amount + that.amount, this.currency)

  /** Subtraction of Money - Must be on the safe type of currency */
  def -[C] (that: Money[C])(using =:=[C,T]): Money[T] = Money[T](this.amount - that.amount, this.currency)
  //def -(that: Money[T]): Money[T] = Money[T](this.amount - that.amount, this.currency)

  /** Multiplication of Money */
  def * (value: MoneyAmount):  Money[T] = Money[T](this.amount * value, this.currency)
  
  /** Multiplication of Money */
  def / (value: MoneyAmount):  Money[T] = Money[T](this.amount / value, this.currency)
  
  /** Remainder after dividing */
  def % (value: MoneyAmount):  Money[T] = Money[T](this.amount % value, this.currency)

  /** Division and Remainder */
  def /% (value: MoneyAmount):  (Money[T], Money[T]) = {
    val aux = this.amount /% value
    (Money[T](aux._1, this.currency), Money[T](aux._2, this.currency))
  }
}
//TODO implement basic opetation for Money (in the same type)
// https://github.com/scala/scala/blob/2.13.x/src/library/scala/math/MoneyAmount.scala