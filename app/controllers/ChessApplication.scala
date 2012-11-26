package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Player
import models.Tournament
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
        Player.create(name, 1200:Double)
        Redirect(routes.ChessApplication.players)
    })
  }
  
  def deletePlayer(id: Long) = Action { implicit request =>
  	Player.delete(id)
  	Redirect(routes.ChessApplication.players)
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
      }
    )
  }
  
  def deleteTournament(id: Long) = Action { implicit request =>
    Tournament.delete(id)
    Redirect(routes.ChessApplication.tournaments)
  }
  
}