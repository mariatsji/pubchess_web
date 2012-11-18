package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import java.sql.Date

case class Tournament()

object Tournament {

  def createSingle(players: List[Player]) =
    if(players.size > 1) {
	    for {
	      player <- players
	      step <- 1 until players.size
	      if((players.indexOf(player) + step) < players.length)
	    } yield new Match(player, players(players.indexOf(player) + step))
    } else {
      Nil
    }
}