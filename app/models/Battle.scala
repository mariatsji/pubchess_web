package models

import anorm.SqlParser._
import java.util.Date
import anorm.~

class Match(white: Player, black: Player, var result: Int, tournament: Tournament) {

  def this(p: Pairing, t: Tournament) = this(p.a, p.b, -1, t)

  def setResult(i: Int) = result = i

}

object Match {

  /**
   * The rowparser
   */
  val match1 = {
    get[Player]("white") ~
      get[Player]("black") ~
      get[Int]("result") ~
      get[Tournament]("tournament") map {
      case white ~ black ~ result ~ tournament => new Match(white, black, result, tournament)
    }
  }

}