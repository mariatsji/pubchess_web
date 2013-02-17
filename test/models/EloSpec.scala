package models

import org.specs2.mutable._
import play.api.test.FakeApplication
import play.api.test.Helpers._

class EloSpec extends Specification {

  "A win for player 1" should {
    "increase ELO for player 1" in {
      running(FakeApplication()) {
        Player.create("1")
        Player.create("2")
        Tournament.create("test", new java.util.Date())
        val whiteWin = new Battle(1, 2, Outcome.WHITE_WIN, 1)
        Elo.calculate(Player.getById(1), Player.getById(2), whiteWin)._1 must beGreaterThan(Elo.DEFAULT)
      }
    }
    "decrease ELO for player 2" in {
      running(FakeApplication()) {
        Player.create("1")
        Player.create("2")
        Tournament.create("test", new java.util.Date())
        val whiteWin = new Battle(1, 2, Outcome.WHITE_WIN, 1)
        Elo.calculate(Player.getById(1), Player.getById(2), whiteWin)._2 must beLessThan(Elo.DEFAULT)
      }
    }
  }

  "A draw for two equally rated players" should {
    "not change ELO for player 1" in {
      running(FakeApplication()) {
        Player.create("1")
        Player.create("2")
        Tournament.create("test", new java.util.Date())
        val whiteWin = new Battle(1, 2, Outcome.DRAW, 1)
        Elo.calculate(Player.getById(1), Player.getById(2), whiteWin)._1 must beEqualTo(Elo.DEFAULT)
      }
    }
    "not change ELO for player 2" in {
      running(FakeApplication()) {
        Player.create("1")
        Player.create("2")
        Tournament.create("test", new java.util.Date())
        val whiteWin = new Battle(1, 2, Outcome.DRAW, 1)
        Elo.calculate(Player.getById(1), Player.getById(2), whiteWin)._2 must beEqualTo(Elo.DEFAULT)
      }
    }
  }

}
