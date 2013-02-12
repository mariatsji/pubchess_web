package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import java.util.Date

object ChessApplication extends Controller {

  def players = Action {
    Ok(views.html.players(Player.all(), playerForm))
  }

  def battles(tournamentId: Long) = Action {
    Ok(views.html.start(Tournament.getOne(tournamentId), Battle.allInTournament(tournamentId)))
  }

  val playerForm = Form("name" -> nonEmptyText)

  def newPlayer = Action { implicit request =>
    playerForm.bindFromRequest.fold(
      errors => BadRequest("Bad request " + errors),
      name => {
        Player.create(name)
        Redirect(routes.ChessApplication.players())
      })
  }

  def deletePlayer(id: Long) = Action { implicit request =>
    Player.delete(id)
    Redirect(routes.ChessApplication.players())
  }

  def startTournamentSingle(tournamentId: Long) = Action { implicit request =>
    val playerIds: List[Long] = try {
      request.queryString("selectedplayers").map(
        s => s.toLong).toList
    } catch {
      case e: NoSuchElementException => List()
    }
    val tournament = Tournament.getOne(tournamentId)
    val pairings = Tournament.createSinglePairings(Player.getSome(playerIds))
    val battles = Battle.createBattles(pairings, tournament).flatten
    if (battles.length > 0) {
      Ok(views.html.start(Tournament.getOne(tournamentId), battles))
    } else {
      Ok("No battles were generated, did you select any players?")
    }
  }

  def setResult(battle: Battle) = Action {
    implicit request =>
      Ok("Awesome")
  }

  def tournaments = Action {
    Ok(views.html.tournaments(Tournament.all(), tournamentForm))
  }

  def addPlayers(id: Long) = Action {
    Ok(views.html.addplayers(Player.all(), Tournament.getOne(id)))
  }

  val tournamentForm = Form("name" -> nonEmptyText)

  def newTournament = Action { implicit request =>
    tournamentForm.bindFromRequest.fold(
      errors => BadRequest("Bad request " + errors),
      name => {
        Tournament.create(name, new Date())
        Redirect(routes.ChessApplication.tournaments())
      })
  }

  def deleteTournament(id: Long) = Action { implicit request =>
    Tournament.delete(id)
    Redirect(routes.ChessApplication.tournaments())
  }

}