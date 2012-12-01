package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import scala.util.Random

case class Pairing(var a:Player, var b:Player) {
  
  override def toString() = {
    a + " paired with " +  b 
  }
 
  def swap():Pairing = {
    new Pairing(this.b, this.a)
  }
  
}

object Pairing {
  
  def shuffle(pairings: List[Pairing]) = Random.shuffle(pairings)
  
}