package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import java.sql.Date

case class Tournament(id: Long, desc: String, var matches: List[Match], var played: Date)

object Tournament {

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