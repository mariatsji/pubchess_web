package models

import org.specs2.mutable._

class EloSpec extends Specification {

  val p1 = new Player(1, "1")
  val p2 = new Player(2, "2")
  val battle = new Battle(p1.id, p2.id, Outcome.WHITE_WIN, -1)

  "A win for player 1" should {
    "increase ELO for player 1" in {
      Elo.calculate(p1, battle) must beGreaterThan(Elo.DEFAULT)
    }
  }

}
