package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import java.util.Date
import scala.collection.immutable.HashSet
import scala.util.Sorting

case class Tournament(id: Long, desc: String, var played: Date)

class Standing(player: Player, points: Float) extends Ordered[Standing] {

  def getPoints = points
  def getPlayer = player

  def compare(that: Standing) = (that.getPoints*2).toInt - (this.getPoints*2).toInt

}

object Tournament {

  def createDoublePairings(players: List[Player]): List[Pairing] = {
    val single = createSinglePairings(players)
    single ::: single.map(_.swapped())
  }

  def createSinglePairings(players: List[Player]): List[Pairing] = {
    chopFromBothEnds(swapEveryOther(naivePairs(players)))
  }

  private def naivePairs(players: List[Player]): List[Pairing] =
    if(players.size > 1)
      players.map{
        (p: Player) => new Pairing(players.head, p)
      }.filter(pairing =>pairing.a != pairing.b) ::: naivePairs(players.tail)
    else
      Nil

  private def swapEveryOther(pairs: List[Pairing]): List[Pairing] = {
    pairs.map((p: Pairing) => if (pairs.indexOf(p) % 2 == 0) p.swapped() else p)
  }

  private def chopFromBothEnds[A](pairs: List[A]): List[A] = {
    if (pairs.isEmpty)
      Nil
    else
      pairs.head :: chopFromBothEnds(pairs.tail.reverse)
  }

  def createPairings(players: List[Player], double: Boolean) =
    if (double) createDoublePairings(players) else createSinglePairings(players)

  def makeRematches(battles: List[Battle]) : List[Battle] = {
    val pairings: List[Pairing] = battles.map(_.getPairing())
    val tournament = TournamentDB.getById(battles.head.tournament)
    val reversePairings: List[Pairing] = pairings.map(_.swapped())
    BattleDB.createBattles(reversePairings, tournament)
  }

  def isCompleted(tournament: Tournament) : Boolean = {
    val battles: List[Battle] = BattleDB.allInTournament(tournament.id)
    battles.filter(b => b.result == (-1)).size==0
  }

  def standings(tournament: Tournament) : List[Standing] = {
    val s: List[Standing] = standings(BattleDB.allInTournament(tournament.id))
    val standingArray: Array[Standing] = s.toArray
    Sorting.quickSort(standingArray)
    standingArray.toList
  }

  def standings(battles: List[Battle]) : List[Standing] = {
    val allPlayers: List[Player] = distinct(battles)
    val allPoints: List[Float] = allPlayers.map(points(_, battles))
    val zipped: List[(Player, Float)] = allPlayers.zip(allPoints)
    zipped.map(makeStanding)
  }

  private def makeStanding(t: (Player, Float)) : Standing = new Standing(t._1, t._2)

  private def distinct(battles: List[Battle]) : List[Player] = {
    val whites: List[Long] = battles.map((b: Battle) => b.white)
    val blacks: List[Long] = battles.map((b: Battle) => b.black)
    val all = whites ::: blacks
    val allSet: HashSet[List[Long]] = HashSet(all)
    PlayerDB.getByIds(allSet.toList.flatten)
  }

  private def points(player: Player, battles : List[Battle]) : Float = {
    val matchesForPlayer: List[Battle] = battles.filter((b: Battle) => b.white == player.id || b.black == player.id)
    matchesForPlayer.foldLeft(0f)((r,c) => r + matchPoint(player, c))
  }

  private def matchPoint(player: Player, battle: Battle) : Float = {
    if(battle.result == 0)
      0.5f
    else {
      if(player.id == battle.white) {
        if (battle.result == 1)
          1f
        else
          0f
      } else {
        if(battle.result == 2)
          1f
        else
          0f
      }
    }
  }

  def sort(standings: List[Standing]) : List[Standing] = {
    standings
  }

}

object TournamentDB {

  /**
   * The rowparser
   */
  val tournament = {
    get[Long]("id") ~
      get[String]("desc") ~
      get[Date]("played") map {
      case id ~ desc ~ played => new Tournament(id, desc, played)
    }
  }

  def all(): List[Tournament] = DB.withConnection {
    implicit c =>
      SQL("SELECT * FROM tournament").as(tournament *)
  }

  def getById(id: Long): Tournament = DB.withConnection {
    implicit c =>
      SQL("SELECT * FROM tournament WHERE id={id}").on("id" -> id).as(tournament *).head
  }

  def create(desc: String, played: Date): Tournament = {
    DB.withConnection {
      implicit c =>
        SQL("INSERT INTO tournament (desc, played) VALUES ({desc}, {played})")
          .on("desc" -> desc, "played" -> played).executeInsert()
    } match {
      case Some(id: Long) => getById(id)
      case None => throw new Exception(
        "SQL Error - Did not save Tournament"
      )
    }
  }

  def delete(id: Long) {
    DB.withConnection {
      implicit c =>
        SQL("DELETE FROM tournament WHERE id={id}")
          .on("id" -> id).executeUpdate()
    }
  }

}