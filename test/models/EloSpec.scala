package models

import org.specs2.mutable._

class EloSpec extends Specification {

  "A win for player 1" should {
    "increase ELO for player 1" in {
      Elo.calculate(1200, 1200, Outcome.WHITE_WIN, 30, 30)._1 must beGreaterThan(Elo.DEFAULT)
    }
    "decrease ELO for player 2" in {
      Elo.calculate(1200, 1200, Outcome.WHITE_WIN, 30, 30)._2 must beLessThan(Elo.DEFAULT)
    }
  }

  "A draw for two equally rated players" should {
    "not change ELO for player 1" in {
      Elo.calculate(1200, 1200, Outcome.DRAW, 30, 30)._1 must beEqualTo(Elo.DEFAULT)
    }
    "not change ELO for player 2" in {
      Elo.calculate(1200, 1200, Outcome.DRAW, 30, 30)._2 must beEqualTo(Elo.DEFAULT)
    }
  }

  "Two wins against unequally rated players" should {
    "give more Elo for win against strong player" in {
      val smallWin = Elo.calculate(1200, 1000, Outcome.WHITE_WIN, 30, 30)._1
      val bigWin = Elo.calculate(1200, 1600, Outcome.WHITE_WIN, 30, 30)._1
      bigWin must beGreaterThan(smallWin)
    }
  }

}
