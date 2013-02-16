package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Player(val id: Long, val name: String) {

  override def toString = name

}

object Player {

  /**
   * The rowparser
   */
  val player = {
    get[Long]("id") ~
      get[String]("name") map {
      case id ~ name => Player(id, name)
    }
  }

  def all(): List[Player] = DB.withConnection {
    implicit c =>
      SQL("SELECT * FROM PLAYER").as(player *)
  }

  def create(name: String): Long = {
    DB.withConnection {
      implicit c =>
        SQL("INSERT INTO PLAYER (name) VALUES ({name})").on(
          "name" -> name).executeInsert()
    }
  } match {
    case Some(primaryKey: Long) => primaryKey
    case None => -1
  }

  def delete(id: Long) {
    DB.withConnection {
      implicit c =>
        SQL("DELETE FROM PLAYER WHERE id = {id}").on("id" -> id).executeUpdate()
    }
  }

  def update(id: Long, name: String) {
    DB.withConnection {
      implicit c =>
        SQL("UPDATE PLAYER SET name={name} WHERE id={id}").on("id" -> id, "name" -> name).executeUpdate()
    }
  }

  def getByIds(ids: List[Long]): List[Player] = {
    Player.all().filter(p => ids.contains(p.id))
  }

  def getById(id: Long): Player = {
    DB.withConnection {
      implicit c =>
        SQL("SELECT * FROM PLAYER WHERE id={id}").on("id"->id).as(player *).head
    }
  }

}