package models

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.Play.current
import anorm.~

case class Elo(val id: Long, val player: Long, val battle: Long, val elo: Double) {

}

object Elo {

  val DEFAULT = 1200F

  def calculate(player: Player, battle: Battle) : Float = {
    1200F
  }

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

  def getById(id: Long): Elo = {
    DB.withConnection {
      implicit c =>
        SQL("SELECT * FROM elo WHERE id={id}").on("id" -> id).as(elo *).head
    }
  }

  def allForPlayer(player: Long): List[Elo] = DB.withConnection {
    implicit c =>
      SQL("SELECT * FROM elo WHERE player={player}").on("player" -> player).as(elo *)
  }

  def create(player: Player, elo: Float) {
    DB.withConnection {
      implicit c =>
        SQL("INSERT INTO elo (player, elo) VALUES ({player},{elo})").on(
          "player" -> player.id, "elo" -> elo).executeUpdate()
    }
  }

  def getCurrent(player: Player): Elo =
    allForPlayer(player.id).last

}
