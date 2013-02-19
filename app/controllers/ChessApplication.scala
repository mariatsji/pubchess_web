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

  def battles(tournamentId: Long) = Action {
    Ok(views.html.started(TournamentDB.getById(tournamentId), BattleDB.allInTournament(tournamentId)))
  }

  val playerForm = Form("name" -> nonEmptyText)

  def newPlayer = Action { implicit request =>
    playerForm.bindFromRequest.fold(
      errors => BadRequest("Bad request " + errors),
      name => {
        val player = Player.create(name)
        EloDB.create(player, Elo.DEFAULT)
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

  def setResult(battle: Battle) = Action {
    implicit request =>
      Ok("Awesome")
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