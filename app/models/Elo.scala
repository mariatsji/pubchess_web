package models

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.Play.current
import anorm.~

case class Elo(id: Long, player: Long, battle: Option[Long], elo: Float) {

}

object Elo {

  val DEFAULT = 1200F

  def calculate(whiteElo: Float, blackElo: Float, outcome: Int, kFactorWhite: Long, kFactorBlack: Long) : (Float,Float) = {
    val qWhite = getQValue(whiteElo)
    val qBlack = getQValue(blackElo)

    val eWhite = qWhite / (qWhite + qBlack)
    val eBlack = qBlack / (qWhite + qBlack)

    val newEloWhite = whiteElo + (kFactorWhite * (getSValue(outcome)._1 - eWhite))
    val newEloBlack = blackElo + (kFactorBlack * (getSValue(outcome)._2 - eBlack))

    (newEloWhite,newEloBlack)
  }

  private def getQValue(elo: Float):Float = math.pow(10, elo / 400).toFloat

  private def getSValue(outcome: Int): (Float,Float) =
    if (outcome == Outcome.WHITE_WIN)
      (1f,0f)
    else if (outcome == Outcome.BLACK_WIN)
      (0f,1f)
    else
      (0.5f, 0.5f)
}

object EloDB {
  /**
    * The rowparser
   */
  val elo = {
    get[Long]("id") ~
    get[Long]("player") ~
    get[Option[Long]]("battle") ~
    get[Double]("elo") map {
      case id ~ player ~ battle ~ elovalue => Elo(id, player, battle, elovalue.toFloat)
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

  def create(player: Player, battle: Option[Battle], elo: Double): Elo = {
    DB.withConnection {
      implicit c =>
        battle match {
          case Some(b) =>
            SQL("INSERT INTO elo (player, battle, elo) VALUES ({player},{battle},{elo})").on(
              "player" -> player.id, "battle" -> b.id, "elo" -> elo).executeInsert()
          case None  =>
            SQL("INSERT INTO elo (player, elo) VALUES ({player},{elo})").on(
              "player" -> player.id, "elo" -> elo).executeInsert()
        }

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
  def getKfactor(player: Player): Long = {
    def playedMatches: Int = BattleDB.allEverMatchesForPlayer(player.id).size
    if (playedMatches < 30) 30
    else {
      if(player.currentElo < 2400) 15 else 10
    }
  }
}
