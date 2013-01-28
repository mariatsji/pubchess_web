package models

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.Play.current
import anorm.~

case class Elo(val id: Long, val player: Long, val battle: Long, val elo: Double) {

}

object Elo {

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

  def allForPlayer(player: Long): List[Elo] = DB.withConnection {
    implicit c =>
      SQL("SELECT * FROM elo WHERE player={player}").on("player" -> player).as(elo *)
  }

  def win(winner: Player, battle: Battle) = {

  }

  def draw(player: Player, battle: Battle) = {

  }

  def lose(loser: Player, battle: Battle) = {

  }

}
