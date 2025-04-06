package ui

import parser.CsvParser
import model._

import scala.io.StdIn.readLine

object ConsoleApp {
  def run(): Unit = {
    val countries = CsvParser.loadCountries("data/countries.csv")
    val airports = CsvParser.loadAirports("data/airports.csv")
    val runways = CsvParser.loadRunways("data/runways.csv")

    println("Bienvenue dans le projet Aéroport.")
    println("Tapez 'Query' pour rechercher ou 'Reports' pour les statistiques :")
    readLine().trim.toLowerCase match {
      case "query" =>
        println("Entrez un nom de pays ou code (ex: France ou FR) :")
        val input = readLine().trim.toLowerCase
        val matchedCountries = countries.filter(c =>
          c.code.toLowerCase == input || c.name.toLowerCase.contains(input)
        )

        matchedCountries.foreach { country =>
          println(s"Aéroports en ${country.name} (${country.code}) :")
          val countryAirports = airports.filter(_.countryCode == country.code)
          countryAirports.foreach { airport =>
            println(s" - ${airport.name}")
            val airportRunways = runways.filter(_.airportRef == airport.id)
            airportRunways.foreach(r => println(s"    * Piste: ${r.surface}"))
          }
        }

      case "reports" =>
        val airportsPerCountry = airports.groupBy(_.countryCode).mapValues(_.size).toList
        val sorted = airportsPerCountry.sortBy(-_._2)

        println("\nTop 10 pays avec le plus d'aéroports :")
        sorted.take(10).foreach { case (code, count) =>
          val countryName = countries.find(_.code == code).map(_.name).getOrElse(code)
          println(s"$countryName: $count")
        }

        println("\nTop 10 pays avec le moins d'aéroports :")
        sorted.reverse.take(10).foreach { case (code, count) =>
          val countryName = countries.find(_.code == code).map(_.name).getOrElse(code)
          println(s"$countryName: $count")
        }

        val surfaceByCountry = airports.map(_.countryCode).distinct.map { code =>
          val airportIds = airports.filter(_.countryCode == code).map(_.id).toSet
          val surfaces = runways.filter(r => airportIds.contains(r.airportRef)).map(_.surface).distinct
          val name = countries.find(_.code == code).map(_.name).getOrElse(code)
          s"$name: ${surfaces.mkString(", ")}"
        }
        println("\nTypes de surfaces de piste par pays :")
        surfaceByCountry.foreach(println)

        val topRunwayIdents = runways.groupBy(_.leIdent).mapValues(_.size).toList
          .filter(_._1.nonEmpty).sortBy(-_._2).take(10)
        println("\nTop 10 identifiants latitude de pistes les plus fréquents :")
        topRunwayIdents.foreach { case (id, count) => println(s"$id: $count") }

      case _ => println("Option invalide.")
    }
  }
}
