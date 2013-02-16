package models

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.Play.current
import anorm.~


case class Pelo(val id: Long, val player: Long, val battle: Long, val pelo: Double) {

}

object Pelo {

  /**
   * The rowparser
   */
  val pelo = {
    get[Long]("id") ~
      get[Long]("player") ~
      get[Long]("battle") ~
      get[Double]("pelo") map {
      case id ~ player ~ battle ~ pelo => Pelo(id, player, battle, pelo)
    }
  }

  def allForPlayer(player: Long): List[Pelo] = DB.withConnection {
    implicit c =>
      SQL("SELECT * FROM pelo WHERE player={player}").on("player" -> player).as(pelo *)
  }

}
