package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import java.util.Date

case class Tournament(id: Long, desc: String, var played: Date)

object Tournament {

    /**
   * The rowparser
   */
  val tournament = {
    get[Long]("id") ~
      get[String]("desc") ~
      	get[Date]("played") map {
      		case id ~ desc ~ played => Tournament(id, desc, played)
      }
  }
  
  def all(): List[Tournament] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM TOURNAMENT").as(tournament *)
  }
  
  def create(desc: String, played: Date) {
    DB.withConnection { implicit c =>
      SQL("INSERT INTO TOURNAMENT (desc, played) VALUES ({desc}, {played})")
      	.on("desc" -> desc, "played" -> played).executeUpdate()
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("DELETE FROM TOURNAMENT WHERE ID={id}")
      	.on("id" -> id).executeUpdate()
    }
  }
  
  def createSingle(players: List[Player]) =
    for {
      player <- players
      step <- 1 until players.size
      if ((players.indexOf(player) + step) < players.length)
    } yield new Pairing(player, players(players.indexOf(player) + step))

  def createDouble(players: List[Player]) = {
      val single = createSingle(players)
      val rev = for {
    	  p <- single
      } yield p.swap
      single ++ rev
  }
    
}