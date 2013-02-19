package models

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.Play.current
import anorm.~

case class Pelo(id: Long, player: Long, battle: Long, pelo: Double) {

}

object PeloDB {

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
