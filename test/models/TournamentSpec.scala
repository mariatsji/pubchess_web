package models

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class TournamentSpec extends Specification {

  "Tournament create uneven numbered tournament" should {
    
    val p1 = new Player(1, "Nils", 1200)
    val p2 = new Player(2, "Truls", 1200)
    val p3 = new Player(3, "Vidar", 1200)
    val p4 = new Player(4, "Hans", 1200)
    val p5 = new Player(5, "Kristian", 1200)
    
    val players = List(p1, p2, p3, p4, p5)
    val matches = Tournament.createSingle(players)
    
    matches.foreach(println)
    "contain 10 matches" in {
      matches must have size(10)
    }
    "contain matches between 1 and 2" in {
      matches(0).white mustEqual(p1)
      matches(0).black mustEqual(p2)
    }
    "contain matches between 1 and 3" in {
      matches(1).white mustEqual(p1)
      matches(1).black mustEqual(p3)
    }
    "contain matches between 1 and 4" in {
      matches(2).white mustEqual(p1)
      matches(2).black mustEqual(p4)
    }
    "contain matches between 1 and 5" in {
      matches(3).white mustEqual(p1)
      matches(3).black mustEqual(p5)
    }
    "contain matches between 2 and 3" in {
      matches(4).white mustEqual(p2)
      matches(4).black mustEqual(p3)
    }
    "contain matches between 2 and 4" in {
      matches(5).white mustEqual(p2)
      matches(5).black mustEqual(p4)
    }
    "contain matches between 2 and 5" in {
      matches(6).white mustEqual(p2)
      matches(6).black mustEqual(p5)
    }
    "contain matches between 3 and 4" in {
      matches(7).white mustEqual(p3)
      matches(7).black mustEqual(p4)
    }
    "contain matches between 3 and 5" in {
      matches(8).white mustEqual(p3)
      matches(8).black mustEqual(p5)
    }
    "contain matches between 4 and 5" in {
      matches(9).white mustEqual(p4)
      matches(9).black mustEqual(p5)
    }
  }
  
  "tournament create even numbered tournament" in {
    val p1 = new Player(1, "a", 1200)
    val p2 = new Player(2, "b", 1200)
    val players = List(p1, p2)
    
    val matches = Tournament.createSingle(players)
    	
    "be of size 1" in {
      matches must have size(1)
    }
    
    "contain match between 1 and 2" in {
      matches(0).white mustEqual(p1)
      matches(0).black mustEqual(p2)
    }
  }
}