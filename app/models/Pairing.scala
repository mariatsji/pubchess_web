package models

case class Pairing(var a: Player, var b: Player) {

  override def toString = a + " paired with " + b
  def swap() = new Pairing(this.b, this.a)
  def swapped() = swap()

}
