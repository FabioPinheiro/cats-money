val dottyLatestNightly = dottyLatestNightlyBuild.get
val dottyVersion = "0.25.0-bin-20200609-a3b417b-NIGHTLY"
//val dottyVersion = "0.24.0-RC1"
val scala213Version = "2.13.2"

inThisBuild(
  Seq(
    organization := "app.fmgp",
    scalaVersion := dottyVersion, //dottyLatestNightly,
    crossScalaVersions := Seq(dottyVersion, dottyLatestNightly),
    updateOptions := updateOptions.value.withLatestSnapshots(false),
  )
)

lazy val modules: List[ProjectReference] = List(core, callEx1, callEx2)

val shapeless3Version = "0.0.0+102-730d9eff-SNAPSHOT"

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8", // source files are in UTF-8
    "-deprecation", // warn about use of deprecated APIs
    "-unchecked", // warn about unchecked type parameters
    "-feature", // warn about misused language features
    "-Xfatal-warnings",
    "-Yexplicit-nulls",
    //"-noindent"
  ),
  sources in (Compile, doc) := Nil,
  libraryDependencies += "org.scalameta" %% "munit" % "0.7.9" % Test,
  testFrameworks += new TestFramework("munit.Framework"),
)

lazy val root = project
  .in(file("."))
  .aggregate(modules: _*)
  .settings(commonSettings: _*)
  .settings(noPublishSettings)

lazy val core = project
  .in(file("modules/core"))
  .settings(name := "cats-money")
  .settings(commonSettings: _*)
  .settings(publishSettings)

lazy val callEx1 = project.in(file("modules/callEx1")).dependsOn(core)
lazy val callEx2 = project.in(file("modules/callEx2")).dependsOn(core)

lazy val sandbox = project
  .in(file("sandbox"))
  .dependsOn(core)
  .settings(
    moduleName := "sandbox",
    scalacOptions ++= List("-Xmax-inlines", "1000"),
    //scalacOptions += "-Xprint:posttyper",
    scalacOptions in console in Compile -= "-Xprint:posttyper",
    libraryDependencies += "org.typelevel" %% "shapeless3-data" % shapeless3Version,
    libraryDependencies += "org.typelevel" %% "shapeless3-deriving" % shapeless3Version,
    libraryDependencies += "org.typelevel" %% "shapeless3-test" % shapeless3Version % Test,
    libraryDependencies += "org.typelevel" %% "shapeless3-typeable" % shapeless3Version,
    initialCommands in console := """
    import shapeless3._ ; import scala.deriving._; 
    import app.fmgp.typeclasses.{_, given _}; import app.fmgp.money.{_, given _};
    """.replaceAll("\n", " ")
  )
  .settings(commonSettings: _*)
  .settings(noPublishSettings)

// lazy val docs = project
//   .in(file("docs-target")) // important: it must not be docs/
//   .settings(
//     //mdoc := run.in(Compile).evaluated,
//     //We need to filter mdoc from the libraryDependencies because mdoc is not publish for dotty
//     libraryDependencies --= Seq("org.scalameta" %% "mdoc" % "2.2.0"),
//     libraryDependencies += ("org.scalameta" %% "mdoc" % "2.2.0")
//       .withDottyCompat(scalaVersion.value),
//     libraryDependencies += ("app.fmgp" %% "cats-money" % "0.1.0-SNAPSHOT"),
//     scalaVersion := dottyVersion,
//     mdocVariables := Map("VERSION" -> version.value),
//     mdocOut := baseDirectory.in(ThisBuild).value / "website" / "docs",
//   )
//   .dependsOn(root)
//   .enablePlugins(MdocPlugin)

lazy val publishSettings = Seq(
  publishArtifact in Test := false,
  pomIncludeRepository := (_ => false),
  homepage := Some(url("https://github.com/FabioPinheiro/cats-money")),
  licenses := Seq("MIT License" -> url("https://opensource.org/licenses/MIT")),
  scmInfo := Some(
    ScmInfo(url("https://github.com/FabioPinheiro/cats-money"), "scm:git:git@github.com:FabioPinheiro/cats-money.git")
  ),
  developers := List(
    Developer("FabioPinheiro", "Fabio Pinheiro", "fabiomgpinheiro@gmail.com", url("http://fmgp.app"))
  )
)

lazy val noPublishSettings = skip in publish := true
