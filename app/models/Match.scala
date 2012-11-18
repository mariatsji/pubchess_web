package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import java.sql.Date

case class Match(white: Player, black:Player){
  override def toString = "[" + white + "] vs [" + black + "]"
}

object Match {

}