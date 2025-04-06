package model
case class Airport(id: String, name: String, countryCode: String)
object Airport {
  def fromCsvLine(line: String): Airport = {
    val cols = line.split(",", -1)
    Airport(cols(0).trim, cols(3).trim, cols(8).trim)
  }
}
