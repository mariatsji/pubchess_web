package models

import anorm.SqlParser._
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

}
