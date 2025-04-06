package parser

import scala.io.Source
import model.{Country, Airport, Runway}

object CsvParser {
  def loadCountries(path: String): List[Country] = {
    Source.fromFile(path).getLines().drop(1).map { line =>
      val cols = line.split(",", -1)
      Country(cols(1).trim, cols(2).trim)
    }.toList
  }

  def loadAirports(path: String): List[Airport] = {
    Source.fromFile(path).getLines().drop(1).map(Airport.fromCsvLine).toList
  }

  def loadRunways(path: String): List[Runway] = {
    Source.fromFile(path).getLines().drop(1).map(Runway.fromCsvLine).toList
  }
}
