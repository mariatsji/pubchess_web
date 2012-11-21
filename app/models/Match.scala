package models

class Match(white: Player, black:Player, var result: Int, tournament: Tournament) {

  def this(p: Pairing, t: Tournament) = this(p.a, p.b, -1, t)
  
  def setResult(i: Int) = result = i
  
}