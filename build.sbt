//val dottyLatestNightly = dottyLatestNightlyBuild.get
//val dottyVersion = dottyLatestNightlyBuild.get
val dottyVersion = "0.26.0-bin-20200617-6b03fbd-NIGHTLY" //Have problems on REPL
//val dottyVersion = "0.25.0-bin-20200609-a3b417b-NIGHTLY"  //Have problems on REPL
//val dottyVersion = "0.24.0-RC1"   //WORKS FINE

inThisBuild(
  Seq(
    organization := "app.fmgp",
    scalaVersion := dottyVersion, //dottyLatestNightly,
    //crossScalaVersions := Seq(dottyVersion, dottyLatestNightly),
    updateOptions := updateOptions.value.withLatestSnapshots(false),
  )
)

lazy val modules: List[ProjectReference] = List(core, callEx1, callEx2)

lazy val root = project
  .in(file("."))
  .aggregate(modules: _*)

lazy val core = project
  .in(file("modules/core"))
  .settings(name := "cats-money")

lazy val callEx1 = project.in(file("modules/callEx1")).dependsOn(core)
lazy val callEx2 = project.in(file("modules/callEx2")).dependsOn(core)
