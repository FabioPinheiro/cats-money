object Demo extends App {

  trait AAA[T]
  //Takes 50s to give a Type Mismatch Error on endlessCompilation
  //given aaaShow[T](using Show[T]) as Show[AAA[T]] = ???

  //Takes more then 10 mins to give the Type Mismatch Error
  implicit def aaaShow[T](implicit showT: cats.Show[T]): cats.Show[AAA[T]] = ???

  import cats.syntax.all._ //From cats: libraryDependencies += ("org.typelevel" %% "cats-core" % "2.2.0-M1").withDottyCompat(scalaVersion.value)

  ("string": Int | Unit) //This will give a Type Mismatch Error
}

// after a `jstack` on the sbt process

// "pool-45-thread-2" #358 prio=5 os_prio=0 cpu=905000.25ms elapsed=920.64s tid=0x00007fee32316800 nid=0x21306 runnable  [0x00007fed49da0000]
//    java.lang.Thread.State: RUNNABLE
// 	at dotty.tools.dotc.core.Types$NamedType.denot(Types.scala:1959)
// 	at dotty.tools.dotc.core.TypeApplications$.safeDealias$extension(TypeApplications.scala:244)
// 	at dotty.tools.dotc.core.TypeApplications$.appliedTo$extension(TypeApplications.scala:284)
// 	at dotty.tools.dotc.core.Types$AppliedType.derivedAppliedType(Types.scala:3917)
// 	at dotty.tools.dotc.core.Types$TypeMap.derivedAppliedType(Types.scala:4831)
// 	at dotty.tools.dotc.core.Types$TypeMap.mapOver(Types.scala:4889)
// 	at dotty.tools.dotc.core.Substituters.substParam(Substituters.scala:143)
// 	at dotty.tools.dotc.core.Contexts$Context.substParam(Contexts.scala:75)
// 	at dotty.tools.dotc.core.Substituters$SubstParamMap.apply(Substituters.scala:189)
// ...
// 	at dotty.tools.dotc.core.Substituters$SubstParamMap.apply(Substituters.scala:189)
// 	at dotty.tools.dotc.core.Types$TypeMap.op$4(Types.scala:4881)
// 	at dotty.tools.dotc.core.Types$TypeMap.mapArgs$1(Types.scala:4881)
// 	at dotty.tools.dotc.core.Types$TypeMap.mapOver(Types.scala:4889)
// 	at dotty.tools.dotc.core.Substituters.substParam(Substituters.scala:143)
// 	at dotty.tools.dotc.core.Contexts$Context.substParam(Contexts.scala:75)
// 	at dotty.tools.dotc.core.Substituters$SubstParamMap.apply(Substituters.scala:189)
// 	at dotty.tools.dotc.core.Types$TypeMap.op$4(Types.scala:4881)
// 	at dotty.tools.dotc.core.Types$TypeMap.mapArgs$1(Types.scala:4881)
// 	at dotty.tools.dotc.core.Types$TypeMap.mapOver(Types.scala:4889)
// 	at dotty.tools.dotc.core.Substituters.substParam(Substituters.scala:143)
// 	at dotty.tools.dotc.core.Contexts$Context.substParam(Contexts.scala:75)
// 	at dotty.tools.dotc.core.Substituters$SubstParamMap.apply(Substituters.scala:189)
// 	at dotty.tools.dotc.core.Types$TypeMap.op$4(Types.scala:4881)
//
