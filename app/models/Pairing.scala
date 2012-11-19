package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Pairing(var a:Player, var b:Player) {
  
  override def toString() = {
    a + " paired with " +  b 
  }
 
  def swap():Pairing = {
    new Pairing(this.b, this.a)
  }
}

object Pairing {
  
  /**
   * The rowparser
   */
  val tournament = {
    get[Long]("id") ~
      get[String]("name") ~
      get[Double]("elo") map {
        case id ~ name ~ elo => Player(id, name, elo)
      }
  }
  
}