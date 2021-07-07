name := "cats-money"
version := "0.1.0"

lazy val root = project
  .in(file("."))
  .settings(
    scalaVersion := "3.0.0",
    //scalacOptions ++= Seq("-language:strictEquality")
  )
