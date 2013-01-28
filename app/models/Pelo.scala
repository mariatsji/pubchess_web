package models

import anorm.SqlParser._
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
      case id ~ player ~ battle ~ pelo => Elo(id, player, battle, pelo)
    }
  }

}
