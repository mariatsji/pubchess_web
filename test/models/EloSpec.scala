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

  "Two wins against unequally rated players" should {
    "give more Elo for win against strong player" in {
      running(FakeApplication()) {
        //player
        Player.create("Player Under Test")
        //strong
        Player.create("Strong player")
        //weak
        Player.create("Weak player")
        Tournament.create("test", new java.util.Date())
        //make palyer 2 strong and 3 weak
        val battle1 = Battle.create(new Pairing(Player.getById(2), Player.getById(3)), Tournament.getById(1))
        val battle2 = Battle.create(new Pairing(Player.getById(2), Player.getById(3)), Tournament.getById(1))
        val battle3 = Battle.create(new Pairing(Player.getById(2), Player.getById(3)), Tournament.getById(1))
        val battle4 = Battle.create(new Pairing(Player.getById(2), Player.getById(3)), Tournament.getById(1))
        val battle5 = Battle.create(new Pairing(Player.getById(2), Player.getById(3)), Tournament.getById(1))
        val battle6 = Battle.create(new Pairing(Player.getById(2), Player.getById(3)), Tournament.getById(1))
        val battle7 = Battle.create(new Pairing(Player.getById(2), Player.getById(3)), Tournament.getById(1))
        val battle8 = Battle.create(new Pairing(Player.getById(2), Player.getById(3)), Tournament.getById(1))
        val battles = List(battle1, battle2, battle3, battle4, battle5, battle6, battle7, battle8)
        1 to 8 foreach(Battle.setResult(_, Outcome.WHITE_WIN))

        //win against weak
        val smallBattle = new Battle(1, 3, Outcome.WHITE_WIN, 1)
        val smallWin = Elo.calculate(Player.getById(1), Player.getById(3), smallBattle)._1
        //win against strong
        val bigBattle = new Battle(1, 2, Outcome.WHITE_WIN, 1)
        val bigWin = Elo.calculate(Player.getById(1), Player.getById(2), bigBattle)._1

        bigWin must beGreaterThan(smallWin)
      }
    }
  }

}
