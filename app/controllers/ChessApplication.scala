package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Player


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
  	Player.delete(id);
  	Redirect(routes.ChessApplication.players)
  }
  
}