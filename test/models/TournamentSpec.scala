package models

import org.specs2.mutable._

class TournamentSpec extends Specification {

  "single tournament createPairings uneven numbered tournament" should {
    val p1 = new Player(1, "1")
    val p2 = new Player(2, "2")
    val p3 = new Player(3, "3")
    val p4 = new Player(4, "4")
    val p5 = new Player(5, "5")
    val players = List(p1, p2, p3, p4, p5)
    val pairings = Tournament.createSinglePairings(players)
    "contain 10 pairings" in {
      pairings must have size (10)
    }
    "contain pairings between 1 and 2" in {
      pairings(0).a mustEqual (p1)
      pairings(0).b mustEqual (p2)
    }
    "contain pairings between 1 and 3" in {
      pairings(1).a mustEqual (p1)
      pairings(1).b mustEqual (p3)
    }
    "contain pairings between 1 and 4" in {
      pairings(2).a mustEqual (p1)
      pairings(2).b mustEqual (p4)
    }
    "contain pairings between 1 and 5" in {
      pairings(3).a mustEqual (p1)
      pairings(3).b mustEqual (p5)
    }
    "contain pairings between 2 and 3" in {
      pairings(4).a mustEqual (p2)
      pairings(4).b mustEqual (p3)
    }
    "contain pairings between 2 and 4" in {
      pairings(5).a mustEqual (p2)
      pairings(5).b mustEqual (p4)
    }
    "contain pairings between 2 and 5" in {
      pairings(6).a mustEqual (p2)
      pairings(6).b mustEqual (p5)
    }
    "contain pairings between 3 and 4" in {
      pairings(7).a mustEqual (p3)
      pairings(7).b mustEqual (p4)
    }
    "contain pairings between 3 and 5" in {
      pairings(8).a mustEqual (p3)
      pairings(8).b mustEqual (p5)
    }
    "contain pairings between 4 and 5" in {
      pairings(9).a mustEqual (p4)
      pairings(9).b mustEqual (p5)
    }
  }

  "single tournament createPairings" should {
    "create even numbered tournament" in {
      val p1 = new Player(1, "a")
      val p2 = new Player(2, "b")
      val players = List(p1, p2)
      val pairings = Tournament.createSinglePairings(players)
      "be of size 1" in {
        pairings must have size (1)
      }
      "contain pairing between 1 and 2" in {
        pairings(0).a mustEqual (p1)
        pairings(0).b mustEqual (p2)
      }
    }
  }
  "tournament with 1 player" should {
    "not crash" in {
      val pairings = Tournament.createSinglePairings(List(new Player(1, "a")))
      "not crash" in {
        pairings must have size (0)
      }
    }
  }
  "tournament with 0 players" should {
    "not crash" in {
      val pairings = Tournament.createSinglePairings(List(new Player(1, "a")))
      "not crash" in {
        pairings must have size (0)
      }
    }
  }

  "double tournament" should {
    val p1 = new Player(1, "1")
    val p2 = new Player(2, "2")
    val p3 = new Player(3, "3")
    val pairings = Tournament.createDoublePairings(List(p1, p2, p3))
    "be size 6" in {
      pairings must have size (6)
    }
    "contain 1 and 2" in {
      pairings(0).a mustEqual (p1)
      pairings(0).b mustEqual (p2)
    }
    "contain 1 and 3" in {
      pairings(1).a mustEqual (p1)
      pairings(1).b mustEqual (p3)
    }
    "contain 2 and 3" in {
      pairings(2).a mustEqual (p2)
      pairings(2).b mustEqual (p3)
    }
    "contain 2 and 1" in {
      pairings(3).a mustEqual (p2)
      pairings(3).b mustEqual (p1)
    }
    "contain 3 and 1" in {
      pairings(4).a mustEqual (p3)
      pairings(4).b mustEqual (p1)
    }
    "contain 3 and 2" in {
      pairings(5).a mustEqual (p3)
      pairings(5).b mustEqual (p2)
    }
  }

  "single random tournament" in {
    val p1 = new Player(1, "1")
    val p2 = new Player(2, "2")
    val p3 = new Player(3, "3")
    val pairings = Tournament.createSingleRandom(List(p1, p2, p3))
    "be size 3" in {
      pairings must have size (3)
    }
  }

  "single random tournament" in {
    val p1 = new Player(1, "1")
    val p2 = new Player(2, "2")
    val p3 = new Player(3, "3")
    val pairings = Tournament.createSingleRandom(List(p1, p2, p3))
    "contain no pairing twice" in {
      pairings(0) must not be pairings(1)
      pairings(0) must not be pairings(2)
      pairings(1) must not be pairings(2)
      pairings(0) must not be pairings(1).swapped()
      pairings(0) must not be pairings(2).swapped()
      pairings(1) must not be pairings(2).swapped()
    }
  }

  "single new tournament implementation" should {
    val p1 = new Player(1, "A")
    val p2 = new Player(2, "B")
    val p3 = new Player(3, "C")
    val p4 = new Player(4, "D")
    val p5 = new Player(5, "E")
    val p6 = new Player(6, "F")

    val players = List(p1,p2,p3,p4,p5,p6)
    val pairings = Tournament.createSinglePairingsNew(players)

    val naivePairings =
      for {
        white <- players
        black <- players.dropWhile(_ != white)
        if (white != black)
      } yield new Pairing(white, black)

    pairings.foreach(println)
    "be size 15" in {
      pairings must have size (15)
    }
    "have all the relevant pairings" in {
      pairings must contain(naivePairings(0).swapped(),
                            naivePairings(1),
                            naivePairings(2).swapped(),
                            naivePairings(3),
                            naivePairings(4).swapped(),
                            naivePairings(5))
    }
  }

}