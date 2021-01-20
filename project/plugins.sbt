addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.5.1")
addSbtPlugin("com.geirsson" % "sbt-ci-release" % "1.5.3")

/** https://github.com/scalameta/scalafmt */
//addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.7.5")

/** https://github.com/codacy/sbt-codacy-coverage#sbt-codacy-coverage */
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")

/** https://maven-badges.herokuapp.com/maven-central/com.codacy/sbt-codacy-coverage */
addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "3.0.3")

/** https://scalameta.org/mdoc/docs/installation.html#sbt */
//addSbtPlugin("org.scalameta" % "sbt-mdoc" % "2.2.0") //not good enough for a dotty project at the moment IMO
