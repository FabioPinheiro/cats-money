name := "cats-money"
version := "0.1.0"

val dottyVersion = "0.24.0-RC1"
val scala213Version = "2.13.2"

lazy val root = project
  .in(file("."))
  .settings(
    scalaVersion := dottyVersion,
    // To cross compile with Dotty and Scala 2
    crossScalaVersions := Seq(dottyVersion, scala213Version)
  )
