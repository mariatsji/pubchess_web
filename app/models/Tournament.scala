package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import java.util.Date

case class Tournament(id: Long, desc: String, var played: Date)

object Tournament {

  def createDoublePairings(players: List[Player]) : List[Pairing] = {
    val single = createSinglePairings(players)
    single ++ single.map(_.swapped())
  }

  def createSinglePairings(players: List[Player]) : List[Pairing] = {
    chopFromBothEnds(swapEveryOther(naivePairs(players)))
  }

  private def naivePairs(players:List[Player]): List[Pairing] = {
    for {
      white <- players
      black <- players.dropWhile(_ != white)
      if(white != black)
    } yield new Pairing(white,black)
  }

  private def swapEveryOther(pairs: List[Pairing]) : List[Pairing] = {
    pairs.map((p: Pairing) => if (pairs.indexOf(p) % 2 == 0) p.swapped() else p)
  }

  private def chopFromBothEnds(pairs: List[Pairing]) : List[Pairing] = {
    def recursive(pairz: List[Pairing], buffer: List[Pairing]) : List[Pairing] =
      if (pairz.isEmpty)
        buffer
      else {
        val reversed = pairz.reverse
        recursive(reversed.tail, (buffer :+ reversed.head))
      }
    recursive(pairs, List[Pairing]())
  }

  def createPairings(players: List[Player], double: Boolean) =
    if (double) createDoublePairings(players) else createSinglePairings(players)

}

  object TournamentDB {

  /**
   * The rowparser
   */
  val tournament = {
    get[Long]("id") ~
      get[String]("desc") ~
      get[Date]("played") map {
      case id ~ desc ~ played => new Tournament(id, desc, played)
    }
  }

  def all(): List[Tournament] = DB.withConnection {
    implicit c =>
      SQL("SELECT * FROM tournament").as(tournament *)
  }

  def getById(id: Long): Tournament = DB.withConnection {
    implicit c =>
      SQL("SELECT * FROM tournament WHERE id={id}").on("id" -> id).as(tournament *).head
  }

  def create(desc: String, played: Date) {
    DB.withConnection {
      implicit c =>
        SQL("INSERT INTO tournament (desc, played) VALUES ({desc}, {played})")
          .on("desc" -> desc, "played" -> played).executeUpdate()
    }
  }

  def delete(id: Long) {
    DB.withConnection {
      implicit c =>
        SQL("DELETE FROM tournament WHERE id={id}")
          .on("id" -> id).executeUpdate()
    }
  }

}