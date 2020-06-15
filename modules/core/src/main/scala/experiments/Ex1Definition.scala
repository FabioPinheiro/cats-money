package experiments

object Ex1Definition {
  opaque type Rate[T] = Double
  object Rate {
    def apply[T](d: Double): Rate[T] = d
    extension Ex1SameNameToBeToImport on [T] (x: Rate[T]) { def value: Double = x }
  }
}


object Ex1InSameFile { //All OK
  import Ex1Definition._
  Rate(1d).value
  Rate(2d).value
}

/* strange compartment
// To reproduce this each line must be run separately on REPL (sbt console)
import experiments.Ex1Definition._                                 //ok
Rate(1d).value                                                     //ok
Rate(1d).value //Does not compile  -> value value is not a member of experiments.Ex1Definition.Rate[Nothing]
Rate(1d).value //Does not compile  -> value value is not a member of experiments.Ex1Definition.Rate[Nothing]
...
import experiments.Ex1Definition._                                 //ok
Rate(1d).value  //Does not compile  -> value value is not a member of experiments.Ex1Definition.Rate[Nothing]
import experiments.Ex1Definition.Rate.Ex1SameNameToBeToImport._    //ok
Rate(1d).value                                                     //ok
Rate(1d).value //Does not compile  -> value value is not a member of experiments.Ex1Definition.Rate[Nothing]
import experiments.Ex1Definition.Rate.Ex1SameNameToBeToImport      //ok Note the '._' difference between the import before!
Rate(1d).value //ok
Rate(1d).value //ok
...



sbt:root> core/console
[info] Compiling 1 Scala source to /home/fabio/workspace/cats-money/modules/core/target/scala-0.25/classes ...

scala> import experiments.Ex1Definition._                                                                                                                                                                                                     

scala> Rate(1d).value                                                                                                                                                                                                                         
val res0: Double = 1.0

scala> Rate(1d).value                                                                                                                                                                                                                         
1 |Rate(1d).value
  |^^^^^^^^^^^^^^
  |value value is not a member of experiments.Ex1Definition.Rate[Nothing]

scala> import experiments.Ex1Definition.Rate.Ex1SameNameToBeToImport._                                                                                                                                                                        

scala> Rate(1d).value
val res1: Double = 1.0

scala> Rate(1d).value                                                                                                                                                                                                                         
1 |Rate(1d).value
  |^^^^^^^^^^^^^^
  |value value is not a member of experiments.Ex1Definition.Rate[Nothing]

scala> import experiments.Ex1Definition.Rate.Ex1SameNameToBeToImport                                                                                                                                                                          

scala> Rate(1d).value
val res2: Double = 1.0

scala> Rate(1d).value                                                                                                                                                                                                                         
val res3: Double = 1.0

*/