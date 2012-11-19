package models

class Match(white: Player, black:Player, var result: Int) {

  def this(p: Pairing) = this(p.a, p.b, -1)
  
  def setResult(i: Int) = result = i
  
}