package models

import org.specs2.mutable._
import play.api.test.FakeApplication
import play.api.test.Helpers._
import java.util.Date

class ChessApplicationSpec extends Specification {

  "Creating a player" should {
    "give back player object with some values" in {
      running(FakeApplication()) {
        val player = Player.create("Test Name")
        player.id must beEqualTo(1)
        player.name must beEqualTo("Test Name")
        player.currentElo must beEqualTo(Elo.DEFAULT)
      }
    }
    "persist a player in the database in" in {
      running(FakeApplication()) {
        Player.create("Test Name")
        PlayerDB.all() must have size(1)
      }
    }
    "create player gettable by its id in" in {
      running(FakeApplication()) {
        val player = Player.create("Test Name")
        val player2 = Player.create("Asdfasdf")
        PlayerDB.getById(player.id).id must beEqualTo(player.id)
        PlayerDB.getByIds(List(player.id, player2.id)) must have size(2)
      }
    }
  }
  "Deleting a player" should {
    "remove player from database" in {
      running(FakeApplication()) {
        val player = Player.create("Test Name")
        PlayerDB.delete(player.id)
        PlayerDB.all() must have size(0)
      }
    }
  }

  "Creating a tournament" should {
    "put a tournament in the database" in {
      running(FakeApplication()) {
        val tournament = TournamentDB.create("test tournament", new Date())
        TournamentDB.getById(tournament.id).id must beEqualTo(tournament.id)
      }
    }
  }

  "Finishing a battle in a tournament" should {
    "update a battle-entry in the database" in {
      running(FakeApplication()) {
        val player1 = Player.create("test 1")
        val player2 = Player.create("test 2")
        val tournament = TournamentDB.create("test tournament", new Date())
        val battle = BattleDB.create(player1.id, player2.id, tournament.id)
        BattleDB.setResult(battle.id, Outcome.BLACK_WIN)
        val battleFound = BattleDB.getById(battle.id)
        battleFound.result must beEqualTo(Outcome.BLACK_WIN)
      }
    }
    "update ELO for players in battle" in {
      running(FakeApplication()) {
        val player1 = Player.create("test 1")
        val player2 = Player.create("test 2")
        val tournament = TournamentDB.create("test tournament", new Date())
        val battle = BattleDB.create(player1.id, player2.id, tournament.id)
        battle.finish(Outcome.BLACK_WIN)
        PlayerDB.getById(player1.id).currentElo must beLessThan(Elo.DEFAULT)
        PlayerDB.getById(player2.id).currentElo must beGreaterThan(Elo.DEFAULT)
      }
    }
    "create elo entries for the players later statistix" in {
      running(FakeApplication()) {
        val player1 = Player.create("test 1")
        val player2 = Player.create("test 2")
        val tournament = TournamentDB.create("test tournament", new Date())
        val battle = BattleDB.create(player1.id, player2.id, tournament.id)
        battle.finish(Outcome.BLACK_WIN)
        EloDB.allForPlayer(player1.id) must be size(2)
        EloDB.allForPlayer(player2.id) must be size(2)
      }
    }

  }

}