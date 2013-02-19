package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

class Battle(val id: Long, val white: Long, val black: Long, var result: Int, val tournament: Long) {

  def setResult(i: Int) {
    result = i
  }

}

object Outcome extends Enumeration {
  val DRAW = 0
  val UNPLAYED = -1
  val WHITE_WIN = 1
  val BLACK_WIN = 2
}

object Battle {

  def createBattles(pairs: List[Pairing], tournament: Tournament) = {
    for {
      pair <- pairs
    } yield create(pair, tournament)
  }

  /**
   * The rowparser
   */
  val battle = {
    get[Long]("id") ~
    get[Long]("white") ~
      get[Long]("black") ~
      get[Int]("result") ~
      get[Long]("tournament") map {
      case id ~ white ~ black ~ result ~ tournament => new Battle(id, white, black, result, tournament)
    }
  }

  def allInTournament(tournament: Long): List[Battle] = DB.withConnection {
    implicit c =>
      SQL("SELECT * FROM battle WHERE tournament={tournament}").on("tournament" -> tournament).as(battle *)
  }

  def allEverMatchesForPlayer(player: Long): List[Battle] = DB.withConnection {
    implicit c =>
      SQL("SELECT * FROM battle WHERE white={white} OR black={black}").on("white" -> player).on("black" -> player).as(battle *)
  }

  def allEverWhiteMatchesForPlayer(player: Long): List[Battle] =
    allEverMatchesForPlayer(player).filter((b: Battle) => (b.black == player): Boolean)

  def allEverBlackMatchesForPlayer(player: Long): List[Battle] =
    allEverMatchesForPlayer(player).filter((b: Battle) => (b.white == player): Boolean)

  def create(white: Long, black: Long, tournament: Long): Battle = {
    DB.withConnection {
      implicit c =>
        SQL("INSERT INTO battle (white,black,result,tournament) VALUES ({white}, {black}, -1, {tournament})")
          .on("white" -> white, "black" -> black).on("tournament" -> tournament).executeInsert()

    } match {
      case Some(id: Long) => Battle.getById(id)
      case None => throw new Exception(
        "SQL Error - Did not save Battle"
      )
    }
  }

  def getById(id: Long): Battle = {
    DB.withConnection {
      implicit c =>
        SQL("SELECT * FROM battle WHERE id={id}").on("id" -> id).as(battle *).head
    }
  }

  def create(pairing: Pairing, tournament: Tournament): Battle = {
    create(pairing.a.id, pairing.b.id, tournament.id)
  }

  def setResult(battleid: Long, result: Int) {
    DB.withConnection {
      implicit c =>
        SQL("UPDATE battle SET result={result} WHERE id={battleid}").on("result" -> result).on("battleid" -> battleid).executeUpdate()

    }
  }


}