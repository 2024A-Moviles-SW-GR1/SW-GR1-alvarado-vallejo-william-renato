package services

import dtos.PlanetasDto
import models.Planeta
import models.SistemaSolar
import java.io.File
import java.time.LocalDate

class PlanetasService {
    private val file: File = File("src/main/resources/planetas.txt").also {
        if (!it.exists()) {
            it.createNewFile()
        }
    }

    companion object {
        private var instance: PlanetasService? = null;

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: PlanetasService().also { instance = it }
        }
    }

    private fun randomString(): String {
        val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..10)
            .map { chars.random() }
            .joinToString("")
    }


    fun getAll(): List<Planeta> {
        val lines = file.readLines()
        val planetas = lines.map { it ->
            val planetasSplit = it.split(",")
            val sistemaSolar = SistemaSolarService.getInstance().getOne(planetasSplit[6]) ?: return@map null

            return@map Planeta(
                planetasSplit[0],
                planetasSplit[1],
                planetasSplit[2],
                planetasSplit[3].toBoolean(),
                planetasSplit[4].toInt(),
                LocalDate.parse(planetasSplit[5]),
                sistemaSolar
            )
        }
        return planetas.filterNotNull()
    }


    fun safeGetAll(): List<Planeta> {
        val lines = file.readLines()
        val planetas = lines.map { it ->
            val planetasSplit = it.split(",")
            val sistemaSolar = SistemaSolarService.getInstance().getOneWithPlanetasWithoutSistemaSolar(planetasSplit[6]) ?: return@map null

            return@map Planeta(
                planetasSplit[0],
                planetasSplit[1],
                planetasSplit[2],
                planetasSplit[3].toBoolean(),
                planetasSplit[4].toInt(),
                LocalDate.parse(planetasSplit[5]),
                sistemaSolar
            )
        }
        return planetas.filterNotNull()
    }

    fun getOne(id: String): Planeta? {
        val lines = file.readLines()
        val planetasString = lines.find { it.split(",")[0] == id } ?: return null

        val planetasSplit = planetasString.split(",")
        val sistemaSolar = SistemaSolarService.getInstance().getOne(planetasSplit[6]) ?: return null

        return Planeta(
            planetasSplit[0],
            planetasSplit[1],
            planetasSplit[2],
            planetasSplit[3].toBoolean(),
            planetasSplit[4].toInt(),
            LocalDate.parse(planetasSplit[5]),
            sistemaSolar
        )
    }

    fun getOneWithoutSistemaSolar(id: String): Planeta? {
        val lines = file.readLines()
        val planetasString = lines.find { it.split(",")[0] == id } ?: return null

        val planetasSplit = planetasString.split(",")

        return Planeta(
            planetasSplit[0],
            planetasSplit[1],
            planetasSplit[2],
            planetasSplit[3].toBoolean(),
            planetasSplit[4].toInt(),
            LocalDate.parse(planetasSplit[5]),
            SistemaSolar()
        )
    }

    fun create(planetas: PlanetasDto): Planeta {
        val newPlanetas = Planeta(
            id = randomString(),
            title = planetas.title,
            genre = planetas.genre,
            isFinished = planetas.isFinished,
            seasons = planetas.seasons,
            emissionDate = planetas.emissionDate,
            sistemaSolar = planetas.sistemaSolar
        )
        file.appendText(newPlanetas.toString() + "\n")
        return newPlanetas
    }

    fun update(planetas: Planeta): Planeta? {
        val lines = file.readLines()
        val planetasString = lines.find { it.split(",")[0] == planetas.getId() } ?: return null
        
        val planetasSplit = planetasString.split(",")

        val newPlanetas = Planeta(
            id = planetasSplit[0],
            title = planetas.getTitle(),
            genre = planetas.getGenre(),
            isFinished = planetas.getIsFinished(),
            seasons = planetas.getSeasons(),
            emissionDate = planetas.getEmissionDate(),
            sistemaSolar = planetas.getSistemaSolar()
        )

        this.remove(planetas.getId())

        file.appendText(newPlanetas.toString() + "\n")

        return newPlanetas
    }

    fun remove(id: String) {
        file.readLines()
            .filter { it.split(",")[0] != id }
            .joinToString("\n", postfix = "\n")
            .also { file.writeText(it) }
    }
}