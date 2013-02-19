package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Battle(id: Long, white: Long, black: Long, var result: Int, tournament: Long) {

  def finish(outcome: Int) {
    BattleDB.setResult(this.id, outcome)
    val white = PlayerDB.getById(this.white)
    val black = PlayerDB.getById(this.black)
    val elo = Elo.calculate(white.currentElo, black.currentElo, outcome, EloDB.getKfactor(white), EloDB.getKfactor(black))
    PlayerDB.update(white.id, white.name, elo._1)
    PlayerDB.update(black.id, black.name, elo._2)
  }

}

object Outcome extends Enumeration {
  val DRAW = 0
  val UNPLAYED = -1
  val WHITE_WIN = 1
  val BLACK_WIN = 2
}

object BattleDB {

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
      case Some(id: Long) => getById(id)
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