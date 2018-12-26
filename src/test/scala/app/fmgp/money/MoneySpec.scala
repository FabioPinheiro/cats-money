package app.fmgp.money

import app.fmgp.money.CurrencyY._
import org.specs2.mutable._

/** testOnly app.fmgp.money.MoneySpec */
class MoneySpec extends Specification {
  "This is the Money Specification".br
  "MoneySpec" >> {
    "create Money" >> {
      val a: MoneyY[CurrencyY.CY] = MoneyY(404, USD)
      val b = MoneyY(20, USD)
      val c = MoneyY(9001, EUR)
      "with currency" >> {
        a.currency must_=== USD
        b.currency must_=== USD
        c.currency must_=== EUR
      }
      "with the right amount" >> {
        a.amount === 404
        b.amount mustEqual 20
        c.amount === 9001
      }
    }
  }
  "MoneyTree" >> {
    "create MoneyTree" >> {
      val m1 = MoneyTree.leaf(MoneyY(1, EUR))
      val m2 = MoneyTree.leaf(MoneyY(2, EUR))
      val m3 = MoneyTree.leaf(MoneyY(3, USD))
      val t1 = MoneyTree.join(m1, m2, m3)
      val t2 = MoneyTree.join(m1, m2, t1)
      val tInts3 = MoneyTree.join[Int](MoneyTree.joinLeaf(1, 2), MoneyTree.leaf(3), MoneyTree.joinLeaf(4, 5))
      "join" >> {
        Seq(t1, t2) must forall(beLike {
          case MoneyZBranch(v) => v post "the number of values inside the tree" must haveSize(3)
        })
      }
      "collectValues" >> {
        tInts3.collectValues must contain(5, 2, 3, 4, 1)
      }
      "collapse" >> {
        tInts3.collapse must beLike {
          case MoneyZBranch(v) => v must {
            v post "the number of values inside the tree" must haveSize(5)
            forall(beAnInstanceOf[MoneyZLeaf[_]])
          }
        }
      }
      "simplify" >> {
        import cats.instances.int.catsKernelStdGroupForInt
        tInts3.simplify.collectValues must contain(5 + 4 + 3 + 2 + 1)
      }
    }
  }
  "Collector" >> {
    "Unsafe collector" >> {
      val m1 = MoneyTree.leaf(MoneyY(1, USD))
      val m2 = MoneyTree.leaf(MoneyY(10, EUR))
      val m3 = MoneyTree.leaf(MoneyY(100, USD))
      val m4 = MoneyTree.leaf(MoneyY(1000, EUR))
      val m5 = MoneyTree.leaf(MoneyY(10000, USD))
      val m6 = MoneyTree.leaf(MoneyY(1, GBP))
      val m7 = MoneyTree.leaf(MoneyY(1, XXX))

      val t1 = MoneyTree.join(m1, m2, m3)
      val t2 = MoneyTree.join(m1, m2, MoneyTree.join(m3), MoneyTree.join(m4, m5))
      val t3 = MoneyTree.join(m1, m2, MoneyTree.join(m3, m6), MoneyTree.join(m4, m5))
      val t4 = MoneyTree.join(m1, m2, MoneyTree.join(m3, m6, m7), MoneyTree.join(m4, m5))
      val c1 = PartialRateConverter.fromMapRates[CY, EUR.type](EUR, Map(USD -> 1.5))
      val c2 = PartialRateConverter.fromMapRates[CY, EUR.type](EUR, Map(USD -> 1.5, GBP -> 0.8))

      import app.fmgp.money.MoneyTree._

      val t2afterConverted: MoneyTree[MoneyY[CurrencyY.EUR.type]] = treeFunctor.map(t2)(e => c1.convert(e))
      val t3afterConverted: MoneyTree[MoneyY[CurrencyY.EUR.type]] = treeFunctor.map(t3)(e => c2.convert(e))
      val t2afterConverted2: MoneyTree[MoneyY[CurrencyY.EUR.type]] = treeMonad.map(t2)(e => c1.convert(e))
      val t3afterConverted2: MoneyTree[MoneyY[CurrencyY.EUR.type]] = treeMonad.map(t3)(e => c2.convert(e))
      /* TODO on the console //WTF BUG (on output on the test)?? try but change c2 to c1 and run this test
      > treeFunctor.map(t3)(e => c1.convert(e))
        scala.MatchError: MoneyY(1,GBP) (of class app.fmgp.money.MoneyY$$anon$1)
          at scala.PartialFunction$$anon$1.apply(PartialFunction.scala:259)
          at scala.PartialFunction$$anon$1.apply(PartialFunction.scala:257)
          at app.fmgp.money.UnsafeRateConverter$$anonfun$1.applyOrElse(Converter.scala:15)
          at app.fmgp.money.UnsafeRateConverter$$anonfun$1.applyOrElse(Converter.scala:15)
          at scala.runtime.AbstractPartialFunction.apply(AbstractPartialFunction.scala:38)
          at app.fmgp.money.UnsafeRateConverter$$anonfun$2.applyOrElse(Converter.scala:21)
          at app.fmgp.money.UnsafeRateConverter$$anonfun$2.applyOrElse(Converter.scala:21)
          at scala.PartialFunction$OrElse.apply(PartialFunction.scala:172)
          at app.fmgp.money.UnsafeRateConverter.convert(Converter.scala:7)
          at .$anonfun$res1$1(<console>:33)
          ...
       */
      "convert a single element" >> {
        c1.convert(MoneyY(1, EUR)) must be_===(MoneyY(1, EUR))
      }
      "after convert must have the some number of elements bur only one currency " >> {
        t2afterConverted.collectValues.map(_.currency) map (e => e must be_===(EUR)) must haveSize(5)
        t2afterConverted2.collectValues.map(_.currency) map (e => e must be_===(EUR)) must haveSize(5)
        t3afterConverted.collectValues.map(_.currency) map (e => e must be_===(EUR)) must haveSize(6)
        t3afterConverted2.collectValues.map(_.currency) map (e => e must be_===(EUR)) must haveSize(6)
      }
      "convert with the right rates" >> {
        t2afterConverted.collectValues.map(_.amount.toDouble) must contain(1.5d, 10d, 150d, 1000d, 15000d)
        t2afterConverted2.collectValues.map(_.amount.toDouble) must contain(1.5d, 10d, 150d, 1000d, 15000d)
        t3afterConverted.collectValues.map(_.amount.toDouble) must contain(1.5d, 10d, 150d, 1000d, 15000d, 0.8)
        t3afterConverted2.collectValues.map(_.amount.toDouble) must contain(1.5d, 10d, 150d, 1000d, 15000d, 0.8)
      }
      "throw an MatchError because of missing Match" >> {
        treeMonad.map(t4)(e => c2.convert(e)) must throwAn[scala.MatchError]
        treeFunctor.map(t4)(e => c2.convert(e)) must throwAn[scala.MatchError]
      }
    }
  }
}
