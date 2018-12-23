package app.fmgp.money

import app.fmgp.money.CurrencyY._
import org.specs2.matcher.ContainWithResult
import org.specs2.mutable._

class MoneySpec extends Specification {
  "This is the Money Specification".br
  "MoneySpec" >> {
    "create Money" >> {
      val a: MoneyY[CurrencyY.CurrencyY] = MoneyY(404, USD)
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
      "collectValues" >> {tInts3.collectValues must contain(5,2,3,4,1)}
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
        tInts3.simplify.collectValues must contain(5+4+3+2+1)
      }
    }
  }
}
