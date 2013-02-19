package models

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.Play.current
import anorm.~

case class Elo(id: Long, player: Long, battle: Long, elo: Double) {

}

object Elo {

  val DEFAULT = 1200D

  def calculate(whiteElo: Double, blackElo: Double, outcome: Int, kFactorWhite: Long, kFactorBlack: Long) : (Double,Double) = {
    val qWhite = math.pow(10, whiteElo / 400)
    val qBlack = math.pow(10, blackElo / 400)

    val eWhite = qWhite / (qWhite + qBlack)
    val eBlack = qBlack / (qWhite + qBlack)

    var sWhite = 0.5
    var sBlack = 0.5
    if (outcome == Outcome.WHITE_WIN) {
      sWhite = 1
      sBlack = 0
    } else if (outcome == Outcome.BLACK_WIN) {
      sWhite = 0
      sBlack = 1
    }
    val newEloWhite = whiteElo + (kFactorWhite * (sWhite - eWhite))
    val newEloBlack = blackElo + (kFactorBlack * (sBlack - eBlack))

    (newEloWhite,newEloBlack)
  }

}

object EloDB {
  /**
    * The rowparser
   */
  val elo = {
    get[Long]("id") ~
    get[Long]("player") ~
    get[Long]("battle") ~
    get[Double]("elo") map {
      case id ~ player ~ battle ~ elo => Elo(id, player, battle, elo)
    }
  }

  def getById(id: Long): Elo = {
    DB.withConnection {
      implicit c =>
        SQL("SELECT * FROM elo WHERE id={id}").on("id" -> id).as(elo *).head
    }
  }

  def allForPlayer(playerId: Long): List[Elo] = DB.withConnection {
    implicit c =>
      SQL("SELECT * FROM elo WHERE player={player}").on("player" -> playerId).as(elo *)
  }

  def create(player: Player, elo: Double): Elo = {
    DB.withConnection {
      implicit c =>
        SQL("INSERT INTO elo (player, elo) VALUES ({player},{elo})").on(
        "player" -> player.id, "elo" -> elo).executeInsert()
    } match {
      case Some(id: Long) => getById(id)
      case None => throw new Exception(
      "SQL Error - Did not save Battle"
    )
    }
  }

  /**
   * FIDE rules:
   * K = 30 (was 25) for a player new to the rating list until s/he has completed events with a total of at least 30 games.[15]
   * K = 15 as long as a player's rating remains under 2400.
   * K = 10 once a player's published rating has reached 2400, and s/he has also completed events with a total of at least 30 games. Thereafter it remains permanently at 10.
   */
  def getKfactor(player: Player): Float = {
    def playedMatches: Int = BattleDB.allEverMatchesForPlayer(player.id).size
    if (playedMatches < 30) 30
    else {
      if(player.currentElo < 2400) 15 else 10
    }
  }
}
