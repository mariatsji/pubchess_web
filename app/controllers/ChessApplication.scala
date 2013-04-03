package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import java.util.Date

object ChessApplication extends Controller {

  def players = Action {
    Ok(views.html.players(PlayerDB.all(), playerForm))
  }

  val setResultForm = Form(tuple(
      "blackbeers" -> text,
      "whitebeers" -> text,
      "winner" -> text
    )
  )

  def battles(tournamentId: Long) = Action {
    Ok(
      views.html.started(TournamentDB.getById(tournamentId), BattleDB.allInTournament(tournamentId))
    )
  }

  def saveResult(battleId: String) = Action { implicit request =>
    val myTuple: (String, String, String) = setResultForm.bindFromRequest().get
    val battle: Battle = BattleDB.getById(battleId.toLong)
    val outcome = myTuple._3 match {
      case "white" => Outcome.WHITE_WIN
      case "black" => Outcome.BLACK_WIN
      case "draw" => Outcome.DRAW
    }
    battle.finish(outcome, myTuple._2.toInt, myTuple._1.toInt)
    Ok("Saved " + outcome + " with # beers white : " + myTuple._2 + " and # beers black : " + myTuple._1)
  }

  val playerForm = Form("name" -> nonEmptyText)

  def newPlayer = Action { implicit request =>
    playerForm.bindFromRequest.fold(
      errors => BadRequest("Bad request " + errors),
      name => {
        val player = Player.create(name)
        EloDB.create(player, None, Elo.DEFAULT)
        Redirect(routes.ChessApplication.players())
      })
  }

  def deletePlayer(id: Long) = Action { implicit request =>
    PlayerDB.delete(id)
    Redirect(routes.ChessApplication.players())
  }

  def startTournamentSingle(tournamentId: Long) = Action { implicit request =>
    val playerIds: List[Long] = try {
      request.queryString("selectedplayers").map(
        s => s.toLong).toList
    } catch {
      case e: NoSuchElementException => List()
    }
    val tournament = TournamentDB.getById(tournamentId)
    val pairings = Tournament.createSinglePairings(PlayerDB.getByIds(playerIds))
    val battles = BattleDB.createBattles(pairings, tournament)
    if (battles.length > 0) {
      Ok(views.html.started(TournamentDB.getById(tournamentId), battles))
    } else {
      Ok("No battles were generated, did you select any players?")
    }
  }

  def tournaments = Action {
    Ok(views.html.tournaments(TournamentDB.all(), tournamentForm))
  }

  def addPlayers(id: Long) = Action {
    Ok(views.html.addplayers(PlayerDB.all(), TournamentDB.getById(id)))
  }

  val tournamentForm = Form("name" -> nonEmptyText)

  def newTournament = Action { implicit request =>
    tournamentForm.bindFromRequest.fold(
      errors => BadRequest("Bad request " + errors),
      name => {
        TournamentDB.create(name, new Date())
        Redirect(routes.ChessApplication.tournaments())
      })
  }

  def deleteTournament(id: Long) = Action { implicit request =>
    TournamentDB.delete(id)
    Redirect(routes.ChessApplication.tournaments())
  }

}