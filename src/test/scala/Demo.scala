import Currency._
//import scala.language.strictEquality

object Demo extends App {
  println(EUR == CHF)
}

// [error] -- Error: /home/fabio/workspace/cats-money/src/test/scala/Demo.scala:5:10 ------
// [error] 5 |  println(EUR == CHF)
// [error]   |          ^^^^^^^^^^
// [error]   |Values of types object Currency.EUR and object Currency.CHF cannot be compared with == or !=
// [error] one error found
// [error] (Test / compileIncremental) Compilation failed

// See https://github.com/lampepfl/dotty/issues/9087
