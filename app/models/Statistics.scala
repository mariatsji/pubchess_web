package models

class Statistics(val player: Player) {

  val battlesAsWhite : List[Battle] = BattleDB.allEverWhiteMatchesForPlayer(player.id)
  val battlesAsBlack : List[Battle] = BattleDB.allEverBlackMatchesForPlayer(player.id)
  val winsAsWhite : Int = battlesAsWhite.filter((b: Battle) => b.result==Outcome.WHITE_WIN).length
  val winsAsBlack : Int = battlesAsBlack.filter((b: Battle) => b.result==Outcome.BLACK_WIN).length
  val drawsAsWhite : Int = battlesAsWhite.filter((b: Battle) => b.result==Outcome.DRAW).length
  val drawsAsBlack : Int = battlesAsBlack.filter((b: Battle) => b.result==Outcome.DRAW).length
  val lossAsWhite : Int = battlesAsWhite.filter((b: Battle) => b.result==Outcome.BLACK_WIN).length
  val lossAsBlack : Int = battlesAsBlack.filter((b: Battle) => b.result==Outcome.WHITE_WIN).length
  val battlesPlayed : Int = battlesAsWhite.length + battlesAsBlack.length
  val currentElo = player.currentElo
  val highestElo = EloDB.allForPlayer(player.id).map(_.elo).max
  val lowestElo = EloDB.allForPlayer(player.id).map(_.elo).min

}
