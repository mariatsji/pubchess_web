package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Player(val id: Long, val name: String, val currentElo: Double) {

  override def toString = name

}

object Player {

  /**
   * The rowparser
   */
  val player = {
    get[Long]("id") ~
      get[String]("name") ~
        get[Double]("currentElo") map {
      case id ~ name ~ currentElo => Player(id, name, currentElo)
    }
  }

  def all(): List[Player] = DB.withConnection {
    implicit c =>
      SQL("SELECT * FROM PLAYER").as(player *)
  }

  def create(name: String): Long = {
    DB.withConnection {
      implicit c =>
        SQL("INSERT INTO PLAYER (name, currentElo) VALUES ({name}, {currentElo})").on(
          "name" -> name, "currentElo" -> Elo.DEFAULT).executeInsert()
    }
  } match {
    case Some(primaryKey: Long) => primaryKey
    case None => throw new Exception(
      "SQL Error - Did not save PlayerL"
    )
  }

  def delete(id: Long) {
    DB.withConnection {
      implicit c =>
        SQL("DELETE FROM PLAYER WHERE id = {id}").on("id" -> id).executeUpdate()
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