package models

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class TournamentSpec extends Specification {

  "single tournament create uneven numbered tournament" should {
    val p1 = new Player(1, "1", 1200)
    val p2 = new Player(2, "2", 1200)
    val p3 = new Player(3, "3", 1200)
    val p4 = new Player(4, "4", 1200)
    val p5 = new Player(5, "5", 1200)
    val players = List(p1, p2, p3, p4, p5)
    val pairings = Tournament.createSingle(players)
    pairings.foreach(println)
    "contain 10 pairings" in {
      pairings must have size(10)
    }
    "contain pairings between 1 and 2" in {
      pairings(0).a mustEqual(p1)
      pairings(0).b mustEqual(p2)
    }
    "contain pairings between 1 and 3" in {
      pairings(1).a mustEqual(p1)
      pairings(1).b mustEqual(p3)
    }
    "contain pairings between 1 and 4" in {
      pairings(2).a mustEqual(p1)
      pairings(2).b mustEqual(p4)
    }
    "contain pairings between 1 and 5" in {
      pairings(3).a mustEqual(p1)
      pairings(3).b mustEqual(p5)
    }
    "contain pairings between 2 and 3" in {
      pairings(4).a mustEqual(p2)
      pairings(4).b mustEqual(p3)
    }
    "contain pairings between 2 and 4" in {
      pairings(5).a mustEqual(p2)
      pairings(5).b mustEqual(p4)
    }
    "contain pairings between 2 and 5" in {
      pairings(6).a mustEqual(p2)
      pairings(6).b mustEqual(p5)
    }
    "contain pairings between 3 and 4" in {
      pairings(7).a mustEqual(p3)
      pairings(7).b mustEqual(p4)
    }
    "contain pairings between 3 and 5" in {
      pairings(8).a mustEqual(p3)
      pairings(8).b mustEqual(p5)
    }
    "contain pairings between 4 and 5" in {
      pairings(9).a mustEqual(p4)
      pairings(9).b mustEqual(p5)
    }
  }
  
  "single tournament create even numbered tournament" in {
    val p1 = new Player(1, "a", 1200)
    val p2 = new Player(2, "b", 1200)
    val players = List(p1, p2)
    val pairings = Tournament.createSingle(players)
    "be of size 1" in {
      pairings must have size(1)
    }
    "contain match between 1 and 2" in {
      pairings(0).a mustEqual(p1)
      pairings(0).b mustEqual(p2)
    }
  }
  "tournament with 1 player shouldnt crash" in {
    val pairings = Tournament.createSingle(List(new Player(1, "a", 1200)))
    "not crash" in {
      pairings must have size(0)
    }
  }
  "tournament with 0 players shouldnt crash" in {
    val pairings = Tournament.createSingle(List(new Player(1, "a", 1200)))
    "not crash" in {
      pairings must have size(0)
    }
  }
  
  "double tournament" in {
    val p1 = new Player(1, "1", 1200)
    val p2 = new Player(2, "2", 1200)
    val p3 = new Player(3, "3", 1200)
    val pairings = Tournament.createDouble(List(p1,p2,p3))
    pairings.foreach(println)
    "be size 6" in {
      pairings must have size(6)
    }
    "contain 1 and 2" in {
      pairings(0).a mustEqual(p1)
      pairings(0).b mustEqual(p2)
    }
    "contain 1 and 3" in {
      pairings(1).a mustEqual(p1)
      pairings(1).b mustEqual(p3)
    }
    "contain 2 and 3" in {
      pairings(2).a mustEqual(p2)
      pairings(2).b mustEqual(p3)
    }
    "contain 2 and 1" in {
      pairings(3).a mustEqual(p2)
      pairings(3).b mustEqual(p1)
    }
    "contain 3 and 1" in {
      pairings(4).a mustEqual(p3)
      pairings(4).b mustEqual(p1)
    }
    "contain 3 and 2" in {
      pairings(5).a mustEqual(p3)
      pairings(5).b mustEqual(p2)
    }
  }
}