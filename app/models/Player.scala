package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Player(id: Long, name: String, currentElo: Float) {

  override def toString = name + "(" + currentElo + ")"

}

object Player {

  def create(name: String): Player = {
    val player = PlayerDB.insert(name)
    EloDB.create(player, None, Elo.DEFAULT)
    player
  }

}

object PlayerDB {

  /**
   * The rowparser
   */
  val player = {
    get[Long]("id") ~
      get[String]("name") ~
      get[Double]("currentElo") map {
      case id ~ name ~ currentElo => Player(id, name, currentElo.toFloat)
    }
  }

  def all(): List[Player] = DB.withConnection {
    implicit c =>
      SQL("SELECT * FROM PLAYER").as(player *)
  }

  def insert(name: String): Player = {
    DB.withConnection {
      implicit c =>
        SQL("INSERT INTO PLAYER (name, currentElo) VALUES ({name}, {currentElo})").on(
          "name" -> name, "currentElo" -> Elo.DEFAULT).executeInsert()
    } match {
      case Some(id: Long) => getById(id)
      case None => throw new Exception(
        "SQL Error - Did not save Battle"
      )
    }
  }

  def delete(id: Long) {
    DB.withConnection {
      implicit c =>
        SQL("DELETE FROM PLAYER WHERE id = {id}").on("id" -> id).executeUpdate()
    }
  }

  def update(id: Long, name: String, currentElo: Double) {
    DB.withConnection { implicit c =>
      SQL("UPDATE PLAYER SET name={name}, currentElo={currentElo} WHERE id={id}").
        on("id" -> id, "name" -> name, "currentElo" -> currentElo).executeUpdate()
    }
  }

  def getByIds(ids: List[Long]): List[Player] = {
    PlayerDB.all().filter(p => ids.contains(p.id))
  }

  def getById(id: Long): Player = {
    DB.withConnection {
      implicit c =>
        SQL("SELECT * FROM PLAYER WHERE id={id}").on("id" -> id).as(player *).head
    }
  }

}