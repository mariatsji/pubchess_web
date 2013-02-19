package models

import org.specs2.mutable._

class TournamentSpec extends Specification {

  "single tournament createPairings uneven numbered tournament" should {
    val p1 = new Player(1, "1", Elo.DEFAULT)
    val p2 = new Player(2, "2", Elo.DEFAULT)
    val p3 = new Player(3, "3", Elo.DEFAULT)
    val p4 = new Player(4, "4", Elo.DEFAULT)
    val p5 = new Player(5, "5", Elo.DEFAULT)
    val players = List(p1, p2, p3, p4, p5)
    val pairings = Tournament.createSinglePairings(players)
    "contain 10 pairings" in {
      pairings must have size (10)
    }
    "contain no pairing where white player is black player" in {
      pairings.filter((p: Pairing) => p.a == p.b).size must beEqualTo(0)
    }
    "contain an equal number of pairings for all players" in {
      pairingsForPlayer(p1, pairings) must beEqualTo(4)
      pairingsForPlayer(p2, pairings) must beEqualTo(4)
      pairingsForPlayer(p3, pairings) must beEqualTo(4)
      pairingsForPlayer(p4, pairings) must beEqualTo(4)
      pairingsForPlayer(p5, pairings) must beEqualTo(4)
    }
    "contain exactly same number of white as black matches for all players" in {
      whitePairingsForPlayer(p1, pairings) must beEqualTo(blackPairingsForPlayer(p1, pairings))
      whitePairingsForPlayer(p2, pairings) must beEqualTo(blackPairingsForPlayer(p2, pairings))
      whitePairingsForPlayer(p3, pairings) must beEqualTo(blackPairingsForPlayer(p3, pairings))
      whitePairingsForPlayer(p4, pairings) must beEqualTo(blackPairingsForPlayer(p4, pairings))
      whitePairingsForPlayer(p5, pairings) must beEqualTo(blackPairingsForPlayer(p5, pairings))
    }
  }

  "single tournament createPairings even numbered tournament" should {
    val p1 = new Player(1, "1", Elo.DEFAULT)
    val p2 = new Player(2, "2", Elo.DEFAULT)
    val p3 = new Player(3, "3", Elo.DEFAULT)
    val p4 = new Player(4, "4", Elo.DEFAULT)
    val players = List(p1, p2, p3, p4)
    val pairings = Tournament.createSinglePairings(players)
    "contain 6 pairings" in {
      pairings must have size (6)
    }
    "contain no pairing where white player is black player" in {
      pairings.filter((p: Pairing) => p.a == p.b).size must beEqualTo(0)
    }
    "contain an equal number of pairings for all players" in {
      pairingsForPlayer(p1, pairings) must beEqualTo(3)
      pairingsForPlayer(p2, pairings) must beEqualTo(3)
      pairingsForPlayer(p3, pairings) must beEqualTo(3)
      pairingsForPlayer(p4, pairings) must beEqualTo(3)
    }
    "contain roughly the same number of white as black matches for all players" in {
      whitePairingsForPlayer(p1, pairings) must beCloseTo(blackPairingsForPlayer(p1, pairings), 1)
      whitePairingsForPlayer(p2, pairings) must beCloseTo(blackPairingsForPlayer(p2, pairings), 1)
      whitePairingsForPlayer(p3, pairings) must beCloseTo(blackPairingsForPlayer(p3, pairings), 1)
      whitePairingsForPlayer(p4, pairings) must beCloseTo(blackPairingsForPlayer(p4, pairings), 1)
    }
  }


    "tournament with 1 player" should {
    "not crash" in {
      val pairings = Tournament.createSinglePairings(List(new Player(1, "a", Elo.DEFAULT)))
      "not crash" in {
        pairings must have size (0)
      }
    }
  }
  "tournament with 0 players" should {
    "not crash" in {
      val pairings = Tournament.createSinglePairings(List(new Player(1, "a", Elo.DEFAULT)))
      "not crash" in {
        pairings must have size (0)
      }
    }
  }

  "double tournament" should {
    val p1 = new Player(1, "1", Elo.DEFAULT)
    val p2 = new Player(2, "2", Elo.DEFAULT)
    val p3 = new Player(3, "3", Elo.DEFAULT)
    val pairings = Tournament.createDoublePairings(List(p1, p2, p3))
    "be size 6" in {
      pairings must have size (6)
    }
  }

  private def whitePairingsForPlayer(player: Player, pairings: List[Pairing]): Int = {
    pairings.foldLeft[Int](0)(
      (sum: Int, pairing: Pairing) => if (pairing.a.id == player.id) sum + 1 else sum
    )
  }

  private def blackPairingsForPlayer(player: Player, pairings: List[Pairing]): Int = {
    whitePairingsForPlayer(player, pairings.map(_.swapped()))
  }

  private def pairingsForPlayer(player: Player, pairings: List[Pairing]): Int = {
    whitePairingsForPlayer(player, pairings) + blackPairingsForPlayer(player, pairings)
  }

}