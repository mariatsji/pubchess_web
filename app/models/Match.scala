package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import java.sql.Date

case class Match(var white: Player, var black:Player){
  
  override def toString = "[" + white + "] vs [" + black + "]"
  
  def swap(){
    val tmp = white
    white = black
    black = tmp
  }
  
}

object Match {
  
}