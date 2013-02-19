package models

import org.specs2.mutable._
import play.api.test.FakeApplication
import play.api.test.Helpers._

class EloSpec extends Specification {

  "A win for player 1" should {
    "increase ELO for player 1" in {
      running(FakeApplication()) {
        PlayerDB.create("1")
        PlayerDB.create("2")
        TournamentDB.create("test", new java.util.Date())
        val whiteWin = new Battle(1, 1, 2, Outcome.WHITE_WIN, 1)
        Elo.calculate(PlayerDB.getById(1), PlayerDB.getById(2), whiteWin)._1 must beGreaterThan(Elo.DEFAULT)
      }
    }
    "decrease ELO for player 2" in {
      running(FakeApplication()) {
        PlayerDB.create("1")
        PlayerDB.create("2")
        TournamentDB.create("test", new java.util.Date())
        val whiteWin = new Battle(1, 1, 2, Outcome.WHITE_WIN, 1)
        Elo.calculate(PlayerDB.getById(1), PlayerDB.getById(2), whiteWin)._2 must beLessThan(Elo.DEFAULT)
      }
    }
  }

  "A draw for two equally rated players" should {
    "not change ELO for player 1" in {
      running(FakeApplication()) {
        PlayerDB.create("1")
        PlayerDB.create("2")
        TournamentDB.create("test", new java.util.Date())
        val whiteWin = new Battle(1, 1, 2, Outcome.DRAW, 1)
        Elo.calculate(PlayerDB.getById(1), PlayerDB.getById(2), whiteWin)._1 must beEqualTo(Elo.DEFAULT)
      }
    }
    "not change ELO for player 2" in {
      running(FakeApplication()) {
        PlayerDB.create("1")
        PlayerDB.create("2")
        TournamentDB.create("test", new java.util.Date())
        val whiteWin = new Battle(1, 1, 2, Outcome.DRAW, 1)
        Elo.calculate(PlayerDB.getById(1), PlayerDB.getById(2), whiteWin)._2 must beEqualTo(Elo.DEFAULT)
      }
    }
  }

  "Two wins against unequally rated players" should {
    "give more Elo for win against strong player" in {
      running(FakeApplication()) {
        //player
        PlayerDB.create("Player Under Test")
        //strong
        PlayerDB.create("Strong player")
        //weak
        PlayerDB.create("Weak player")
        TournamentDB.create("test", new java.util.Date())
        //make palyer 2 strong and 3 weak
        BattleDB.create(new Pairing(PlayerDB.getById(2), PlayerDB.getById(3)), TournamentDB.getById(1))
        BattleDB.create(new Pairing(PlayerDB.getById(2), PlayerDB.getById(3)), TournamentDB.getById(1))
        BattleDB.create(new Pairing(PlayerDB.getById(2), PlayerDB.getById(3)), TournamentDB.getById(1))
        BattleDB.create(new Pairing(PlayerDB.getById(2), PlayerDB.getById(3)), TournamentDB.getById(1))
        BattleDB.create(new Pairing(PlayerDB.getById(2), PlayerDB.getById(3)), TournamentDB.getById(1))
        BattleDB.create(new Pairing(PlayerDB.getById(2), PlayerDB.getById(3)), TournamentDB.getById(1))
        BattleDB.create(new Pairing(PlayerDB.getById(2), PlayerDB.getById(3)), TournamentDB.getById(1))
        BattleDB.create(new Pairing(PlayerDB.getById(2), PlayerDB.getById(3)), TournamentDB.getById(1))
        1 to 8 foreach(BattleDB.setResult(_, Outcome.WHITE_WIN))

        //win against weak
        val smallBattle = new Battle(1, 1, 3, Outcome.WHITE_WIN, 1)
        val smallWin = Elo.calculate(PlayerDB.getById(1), PlayerDB.getById(3), smallBattle)._1
        //win against strong
        val bigBattle = new Battle(1, 1, 2, Outcome.WHITE_WIN, 1)
        val bigWin = Elo.calculate(PlayerDB.getById(1), PlayerDB.getById(2), bigBattle)._1

        bigWin must beGreaterThan(smallWin)
      }
    }
  }

}
