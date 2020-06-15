package experiments

object Ex2Definition {
  opaque type SomeUnrelatedOpaque = Int
  class A[T](val d: T)
  extension Ex2SameNameToBeToImport on [T] (x: A[T]) { def value: T = x.d }
}


object Ex2InSameFile { //All OK
  import experiments.Ex2Definition._
  A(1d).value
  A(2d).value
}

/* strange compartment
sbt:root> core/console

scala> import experiments.Ex2Definition._

scala> A(1d).value                                                                                                                                                                                                                            
exception while typing new experiments.Ex2Definition.A of class class dotty.tools.dotc.ast.Trees$New # 1255532
exception while typing new experiments.Ex2Definition.A of class class dotty.tools.dotc.ast.Trees$Select # 1255533
exception while typing new experiments.Ex2Definition.A[Double] of class class dotty.tools.dotc.ast.Trees$TypeApply # 1255559
exception while typing new experiments.Ex2Definition.A[Double](1.0) of class class dotty.tools.dotc.ast.Trees$Apply # 1255560
exception while typing experiments.Ex2Definition.Ex2SameNameToBeToImport.value[Double](
  new experiments.Ex2Definition.A[Double](1.0)
) of class class dotty.tools.dotc.ast.Trees$Apply # 1255561
exception while typing def res0: Double = 
  experiments.Ex2Definition.Ex2SameNameToBeToImport.value[Double](
    new experiments.Ex2Definition.A[Double](1.0)
  ) of class class dotty.tools.dotc.ast.Trees$DefDef # 1255564
exception while typing @scala.annotation.internal.SourceFile("rs$line$2") final module class rs$line$2$
  (
) extends Object(), Serializable {
  private def writeReplace(): AnyRef = 
    new scala.runtime.ModuleSerializationProxy(classOf[rs$line$2.type])
  def res0: Double = 
    experiments.Ex2Definition.Ex2SameNameToBeToImport.value[Double](
      new experiments.Ex2Definition.A[Double](1.0)
    )
} of class class dotty.tools.dotc.ast.Trees$TypeDef # 1255566
exception while typing package <empty> {
  final lazy module val rs$line$2: rs$line$2$ = new rs$line$2$()
  @scala.annotation.internal.SourceFile("rs$line$2") final module class 
    rs$line$2$
  () extends Object(), Serializable {
    private def writeReplace(): AnyRef = 
      new scala.runtime.ModuleSerializationProxy(classOf[rs$line$2.type])
    def res0: Double = 
      experiments.Ex2Definition.Ex2SameNameToBeToImport.value[Double](
        new experiments.Ex2Definition.A[Double](1.0)
      )
  }
} of class class dotty.tools.dotc.ast.Trees$PackageDef # 1255567
[error] (run-main-d) java.lang.AssertionError: assertion failed: denotation module class Ex2Definition$ invalid in run 2. ValidFor: Period(1..22, run = 3)
[error] java.lang.AssertionError: assertion failed: denotation module class Ex2Definition$ invalid in run 2. ValidFor: Period(1..22, run = 3)
[error]         at dotty.DottyPredef$.assertFail(DottyPredef.scala:17)
[error]         at dotty.tools.dotc.core.Denotations$SingleDenotation.updateValidity(Denotations.scala:688)
[error]         at dotty.tools.dotc.core.Denotations$SingleDenotation.bringForward(Denotations.scala:718)
[error]         at dotty.tools.dotc.core.Denotations$SingleDenotation.current(Denotations.scala:773)
[error]         at dotty.tools.dotc.core.Symbols$Symbol.recomputeDenot(Symbols.scala:498)
[error]         at dotty.tools.dotc.core.Symbols$Symbol.computeDenot(Symbols.scala:493)
[error]         at dotty.tools.dotc.core.Symbols$Symbol.denot(Symbols.scala:487)
[error]         at dotty.tools.dotc.core.SymDenotations.stillValidInOwner(SymDenotations.scala:64)
[error]         at dotty.tools.dotc.core.Contexts$Context.stillValidInOwner(Contexts.scala:79)
[error]         at dotty.tools.dotc.core.SymDenotations.stillValid(SymDenotations.scala:60)
[error]         at dotty.tools.dotc.core.Contexts$Context.stillValid(Contexts.scala:79)
[error]         at dotty.tools.dotc.core.Denotations$SingleDenotation.bringForward(Denotations.scala:718)
[error]         at dotty.tools.dotc.core.Denotations$SingleDenotation.current(Denotations.scala:773)
[error]         at dotty.tools.dotc.core.Symbols$Symbol.recomputeDenot(Symbols.scala:498)
[error]         at dotty.tools.dotc.core.Symbols$Symbol.computeDenot(Symbols.scala:493)
[error]         at dotty.tools.dotc.core.Symbols$Symbol.denot(Symbols.scala:487)
[error]         at dotty.tools.dotc.core.Symbols$.toDenot(Symbols.scala:859)
[error]         at dotty.tools.dotc.core.Types$ClassInfo.force$1(Types.scala:4422)
[error]         at dotty.tools.dotc.core.Types$ClassInfo.refineSelfType$1$$anonfun$1(Types.scala:4432)
[error]         at dotty.tools.dotc.core.Types$LazyRef.ref(Types.scala:2642)
[error]         at dotty.tools.dotc.core.Types$Type.stripLazyRef(Types.scala:1064)
[error]         at dotty.tools.dotc.core.SymDenotations$SymDenotation.recur$2(SymDenotations.scala:1289)
[error]         at dotty.tools.dotc.core.SymDenotations$SymDenotation.opaqueAlias(SymDenotations.scala:1293)
[error]         at dotty.tools.dotc.transform.ElimOpaque.transform(ElimOpaque.scala:36)
[error]         at dotty.tools.dotc.core.Denotations$SingleDenotation.current(Denotations.scala:800)
[error]         at dotty.tools.dotc.core.Symbols$Symbol.recomputeDenot(Symbols.scala:498)
[error]         at dotty.tools.dotc.core.Symbols$Symbol.computeDenot(Symbols.scala:493)
[error]         at dotty.tools.dotc.core.Symbols$Symbol.denot(Symbols.scala:487)
[error]         at dotty.tools.dotc.core.Symbols$Symbol.name(Symbols.scala:626)
[error]         at dotty.tools.dotc.core.Scopes$MutableScope.unlink(Scopes.scala:333)
[error]         at dotty.tools.dotc.core.Scopes$Scope.filteredScope$$anonfun$1(Scopes.scala:164)
[error]         at dotty.runtime.function.JProcedure1.apply(JProcedure1.java:15)
[error]         at dotty.runtime.function.JProcedure1.apply(JProcedure1.java:10)
[error]         at scala.collection.IterableOnceOps.foreach(IterableOnce.scala:553)
[error]         at scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:551)
[error]         at scala.collection.AbstractIterator.foreach(Iterator.scala:1279)
[error]         at dotty.tools.dotc.core.Scopes$Scope.filteredScope(Scopes.scala:165)
[error]         at dotty.tools.dotc.core.TypeErasure.dotty$tools$dotc$core$TypeErasure$$apply(TypeErasure.scala:501)
[error]         at dotty.tools.dotc.core.TypeErasure.eraseInfo(TypeErasure.scala:549)
[error]         at dotty.tools.dotc.core.TypeErasure$.transformInfo(TypeErasure.scala:202)
[error]         at dotty.tools.dotc.transform.Erasure.transform(Erasure.scala:81)
[error]         at dotty.tools.dotc.core.Denotations$SingleDenotation.current(Denotations.scala:800)
[error]         at dotty.tools.dotc.core.Symbols$Symbol.recomputeDenot(Symbols.scala:498)
[error]         at dotty.tools.dotc.core.Symbols$Symbol.computeDenot(Symbols.scala:493)
[error]         at dotty.tools.dotc.core.Symbols$Symbol.denot(Symbols.scala:487)
[error]         at dotty.tools.dotc.core.Symbols$ClassSymbol.classDenot(Symbols.scala:822)
[error]         at dotty.tools.dotc.core.Symbols$.toClassDenot(Symbols.scala:862)
[error]         at dotty.tools.dotc.core.TypeOps$.makePackageObjPrefixExplicit(TypeOps.scala:463)
[error]         at dotty.tools.dotc.typer.TypeAssigner.test$1(TypeAssigner.scala:113)
[error]         at dotty.tools.dotc.typer.TypeAssigner.ensureAccessible(TypeAssigner.scala:117)
[error]         at dotty.tools.dotc.typer.Typer.ensureAccessible(Typer.scala:84)
[error]         at dotty.tools.dotc.typer.Typer.typedNew(Typer.scala:682)
[error]         at dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:2396)
[error]         at dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:2452)
[error]         at dotty.tools.dotc.typer.ReTyper.typedUnadapted(ReTyper.scala:124)
[error]         at dotty.tools.dotc.typer.Typer.op$1(Typer.scala:2518)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2527)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2530)
[error]         at dotty.tools.dotc.transform.Erasure$Typer.typedSelect(Erasure.scala:635)
[error]         at dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:2362)
[error]         at dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:2451)
[error]         at dotty.tools.dotc.typer.ReTyper.typedUnadapted(ReTyper.scala:124)
[error]         at dotty.tools.dotc.typer.Typer.op$1(Typer.scala:2518)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2527)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2530)
[error]         at dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:2641)
[error]         at dotty.tools.dotc.transform.Erasure$Typer.typedTypeApply(Erasure.scala:724)
[error]         at dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:2410)
[error]         at dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:2452)
[error]         at dotty.tools.dotc.typer.ReTyper.typedUnadapted(ReTyper.scala:124)
[error]         at dotty.tools.dotc.typer.Typer.op$1(Typer.scala:2518)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2527)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2530)
[error]         at dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:2641)
[error]         at dotty.tools.dotc.transform.Erasure$Typer.typedApply(Erasure.scala:746)
[error]         at dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:2392)
[error]         at dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:2452)
[error]         at dotty.tools.dotc.typer.ReTyper.typedUnadapted(ReTyper.scala:124)
[error]         at dotty.tools.dotc.typer.Typer.op$1(Typer.scala:2518)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2527)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2530)
[error]         at dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:2641)
[error]         at dotty.tools.dotc.transform.Erasure$Typer.$anonfun$4(Erasure.scala:759)
[error]         at dotty.tools.dotc.core.Decorators$ListDecorator$.zipWithConserve$extension(Decorators.scala:110)
[error]         at dotty.tools.dotc.transform.Erasure$Typer.typedApply(Erasure.scala:759)
[error]         at dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:2392)
[error]         at dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:2452)
[error]         at dotty.tools.dotc.typer.ReTyper.typedUnadapted(ReTyper.scala:124)
[error]         at dotty.tools.dotc.typer.Typer.op$1(Typer.scala:2518)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2527)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2530)
[error]         at dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:2641)
[error]         at dotty.tools.dotc.typer.Typer.typedDefDef(Typer.scala:1916)
[error]         at dotty.tools.dotc.transform.Erasure$Typer.typedDefDef(Erasure.scala:860)
[error]         at dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:2369)
[error]         at dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:2451)
[error]         at dotty.tools.dotc.typer.ReTyper.typedUnadapted(ReTyper.scala:124)
[error]         at dotty.tools.dotc.typer.Typer.op$1(Typer.scala:2518)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2527)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2530)
[error]         at dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:2552)
[error]         at dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:2597)
[error]         at dotty.tools.dotc.transform.Erasure$Typer.typedStats(Erasure.scala:947)
[error]         at dotty.tools.dotc.typer.Typer.typedClassDef(Typer.scala:2050)
[error]         at dotty.tools.dotc.typer.Typer.typedTypeOrClassDef$2(Typer.scala:2380)
[error]         at dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:2384)
[error]         at dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:2451)
[error]         at dotty.tools.dotc.typer.ReTyper.typedUnadapted(ReTyper.scala:124)
[error]         at dotty.tools.dotc.typer.Typer.op$1(Typer.scala:2518)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2527)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2530)
[error]         at dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:2552)
[error]         at dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:2597)
[error]         at dotty.tools.dotc.transform.Erasure$Typer.typedStats(Erasure.scala:947)
[error]         at dotty.tools.dotc.typer.Typer.typedPackageDef(Typer.scala:2176)
[error]         at dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:2424)
[error]         at dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:2452)
[error]         at dotty.tools.dotc.typer.ReTyper.typedUnadapted(ReTyper.scala:124)
[error]         at dotty.tools.dotc.typer.Typer.op$1(Typer.scala:2518)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2527)
[error]         at dotty.tools.dotc.typer.Typer.typed(Typer.scala:2530)
[error]         at dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:2641)
[error]         at dotty.tools.dotc.transform.Erasure.run(Erasure.scala:121)
[error]         at dotty.tools.dotc.core.Phases$Phase.runOn$$anonfun$1(Phases.scala:318)
[error]         at scala.collection.immutable.List.map(List.scala:246)
[error]         at dotty.tools.dotc.core.Phases$Phase.runOn(Phases.scala:319)
[error]         at dotty.tools.dotc.Run.runPhases$4$$anonfun$4(Run.scala:166)
[error]         at dotty.runtime.function.JProcedure1.apply(JProcedure1.java:15)
[error]         at dotty.runtime.function.JProcedure1.apply(JProcedure1.java:10)
[error]         at scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1323)
[error]         at dotty.tools.dotc.Run.runPhases$5(Run.scala:176)
[error]         at dotty.tools.dotc.Run.compileUnits$$anonfun$1(Run.scala:184)
[error]         at dotty.runtime.function.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:12)
[error]         at dotty.tools.dotc.util.Stats$.maybeMonitored(Stats.scala:64)
[error]         at dotty.tools.dotc.Run.compileUnits(Run.scala:191)
[error]         at dotty.tools.dotc.Run.compileUnits(Run.scala:133)
[error]         at dotty.tools.repl.ReplCompiler.runCompilationUnit(ReplCompiler.scala:156)
[error]         at dotty.tools.repl.ReplCompiler.compile(ReplCompiler.scala:166)
[error]         at dotty.tools.repl.ReplDriver.compile(ReplDriver.scala:231)
[error]         at dotty.tools.repl.ReplDriver.interpret(ReplDriver.scala:195)
[error]         at dotty.tools.repl.ReplDriver.loop$1(ReplDriver.scala:128)
[error]         at dotty.tools.repl.ReplDriver.runUntilQuit$$anonfun$1(ReplDriver.scala:131)
[error]         at dotty.tools.repl.ReplDriver.withRedirectedOutput(ReplDriver.scala:150)
[error]         at dotty.tools.repl.ReplDriver.runUntilQuit(ReplDriver.scala:131)
[error]         at xsbt.ConsoleInterface.run(ConsoleInterface.java:52)
[error]         at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
[error]         at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
[error]         at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
[error]         at java.base/java.lang.reflect.Method.invoke(Method.java:566)
[error]         at sbt.internal.inc.AnalyzingCompiler.call(AnalyzingCompiler.scala:248)
[error]         at sbt.internal.inc.AnalyzingCompiler.console(AnalyzingCompiler.scala:210)
[error]         at sbt.Console.console0$1(Console.scala:48)
[error]         at sbt.Console.$anonfun$apply$2(Console.scala:51)
[error]         at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:23)
[error]         at sbt.util.InterfaceUtil$$anon$1.get(InterfaceUtil.scala:10)
[error]         at sbt.TrapExit$App.run(TrapExit.scala:257)
[error]         at java.base/java.lang.Thread.run(Thread.java:834)
[error] Nonzero exit code: 1
[error] (core / Compile / console) Nonzero exit code: 1


*/