package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Player(id: Long, name: String, elo: Double) {

}

object Player {

  /**
   * The rowparser
   */
  val player = {
    get[Long]("id") ~
      get[String]("name") ~
      get[Double]("elo") map {
        case id ~ name ~ elo => Player(id, name, elo)
      }
  }

  def all(): List[Player] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM PLAYER").as(player *)
  }

  def create(name: String, elo: Double) {
    DB.withConnection { implicit c =>
      SQL("INSERT INTO PLAYER (name, elo) VALUES ({name}, {elo})").on(
        "name" -> name, "elo" -> elo).executeUpdate()
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("DELETE FROM PLAYER WHERE id = {id}").on("id" -> id).executeUpdate()
    }
  }

  def update(id: Long, name: String, elo: Double) {
    DB.withConnection { implicit c =>
      SQL("UPDATE PLAYER SET name={name}, elo={elo} WHERE id={id}").on("id" -> id, "name" -> name, "elo" -> elo).executeUpdate()
    }
  }

}