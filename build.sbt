name := "cats-money"
version := "0.1.0"

val dottyVersion = "0.23.0-RC1"
val scala213Version = "2.13.1"

scalacOptions ++= Seq(
  "-encoding",
  "UTF-8", // source files are in UTF-8
  "-deprecation", // warn about use of deprecated APIs
  "-unchecked", // warn about unchecked type parameters
  "-feature", // warn about misused language features
  "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
  //"-Xlint",               // enable handy linter warnings
  //"-Xfatal-warnings",     // turn compiler warnings into errors
  // "-Ypartial-unification", // allow the compiler to unify type constructors of different arities
  // "-Ypatmat-exhaust-depth", "40",
)

//scalacOptions in Test ++= Seq("-Yrangepos")

//addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")

lazy val root = project
  .in(file("."))
  .settings(
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",
    libraryDependencies ++= Seq(
      ("org.typelevel" %% "cats-core" % "2.2.0-M1").withDottyCompat(scalaVersion.value),
      ("com.chuusai" %% "shapeless" % "2.3.3").withDottyCompat(scalaVersion.value),
      // "org.typelevel" %% "cats-laws" % "1.5.0" % Test, //or `cats-testkit` if you are using ScalaTest
      // "org.typelevel" %% "cats-testkit" % "1.5.0" % Test,
      // "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.0" % Test,
      // "org.specs2" %% "specs2-core" % "4.3.4" % Test
    ),
    // To make the default compiler and REPL use Dotty
    scalaVersion := dottyVersion,
    // To cross compile with Dotty and Scala 2
    crossScalaVersions := Seq(dottyVersion, scala213Version)
  )
