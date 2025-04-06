package model
case class Runway(id: String, airportRef: String, surface: String, leIdent: String)
object Runway {
  def fromCsvLine(line: String): Runway = {
    val cols = line.split(",", -1)
    Runway(cols(0).trim, cols(1).trim, cols(5).trim, cols(8).trim)
  }
}
