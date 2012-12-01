package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import java.util.Date

object ChessApplication extends Controller {

  def players = Action {
    Ok(views.html.players(Player.all(), playerForm))
  }

  val playerForm = Form("name" -> nonEmptyText)

  def newPlayer = Action { implicit request =>
    playerForm.bindFromRequest.fold(
      errors => BadRequest("Bad request " + errors),
      name => {
        Player.create(name, 1200: Double)
        Redirect(routes.ChessApplication.players)
      })
  }

  def deletePlayer(id: Long) = Action { implicit request =>
    Player.delete(id)
    Redirect(routes.ChessApplication.players)
  }

  def startTournament(tournamentId: Long) = Action { implicit request =>
    val playerIds: List[Long] = try {
      request.queryString("selectedplayers").map(
        s => s.toLong).toList
    } catch {
      case e: NoSuchElementException => List()
    }
    val isDouble = try {
      request.queryString("double") match {
        case _ => true
      }
    } catch {
      case e: NoSuchElementException => false;
    }
    val pairings = Tournament.create(
      Player.getSome(playerIds),
      isDouble);
    if (playerIds.length > 0) {
      Ok(views.html.start(Tournament.getOne(tournamentId), pairings))
    } else {
      Ok("No players added, did you select any?")
    }
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
        Redirect(routes.ChessApplication.tournaments)
      })
  }

  def deleteTournament(id: Long) = Action { implicit request =>
    Tournament.delete(id)
    Redirect(routes.ChessApplication.tournaments)
  }

}