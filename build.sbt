name := "cats-money"
version := "0.0.1"

scalaVersion := "2.12.8"

scalacOptions ++= Seq(
  "-encoding", "UTF-8",   // source files are in UTF-8
  "-deprecation",         // warn about use of deprecated APIs
  "-unchecked",           // warn about unchecked type parameters
  "-feature",             // warn about misused language features
  "-language:higherKinds",// allow higher kinded types without `import scala.language.higherKinds`
//  "-Xlint",               // enable handy linter warnings
//  "-Xfatal-warnings",     // turn compiler warnings into errors
  "-Ypartial-unification" // allow the compiler to unify type constructors of different arities
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.5.0",
  "org.typelevel" %% "cats-laws" % "1.5.0" % Test, //or `cats-testkit` if you are using ScalaTest
  "org.typelevel" %% "cats-testkit" % "1.5.0" % Test,
  "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.0" % Test,
  "org.specs2" %% "specs2-core" % "4.3.4" % Test
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")
