package services

import dtos.SistemaSolarDto
import models.Planeta
import models.SistemaSolar
import java.io.File

class SistemaSolarService {
    private val file: File = File("src/main/resources/sistemaSolars.txt").also {
        if (!it.exists()) {
            it.createNewFile()
        }
    }

    companion object {
        private var instance: SistemaSolarService? = null;

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: SistemaSolarService().also { instance = it }
        }
    }

    private fun randomString(): String {
        val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..10)
            .map { chars.random() }
            .joinToString("")
    }


    fun getAll(): List<SistemaSolar> {
        val lines = file.readLines()
        val sistemaSolars = lines.map { it ->
            val sistemaSolarSplit = it.split(",")
            // get planetas from an array of ids
            val planetas = sistemaSolarSplit[4].split(";")
                .map { PlanetasService.getInstance().getOne(it) }

            val validPlanetas = planetas.filterNotNull()
            return@map SistemaSolar(
                sistemaSolarSplit[0],
                sistemaSolarSplit[1],
                sistemaSolarSplit[2],
                sistemaSolarSplit[3].toDouble(),
                validPlanetas.toMutableList()
            )
        }
        return sistemaSolars
    }

    fun safeGetAll(): List<SistemaSolar> {
        val lines = file.readLines()
        val sistemaSolars = lines.map { it ->
            val sistemaSolarSplit = it.split(",")
            // get planetas from an array of ids
            val planetas = sistemaSolarSplit[4].split(";")
                .map { PlanetasService.getInstance().getOneWithoutSistemaSolar(it) }

            val validPlanetas = planetas.filterNotNull()
            return@map SistemaSolar(
                sistemaSolarSplit[0],
                sistemaSolarSplit[1],
                sistemaSolarSplit[2],
                sistemaSolarSplit[3].toDouble(),
                validPlanetas.toMutableList()
            )
        }
        return sistemaSolars
    }

    fun getOne(id: String): SistemaSolar? {
        val lines = file.readLines()
        val sistemaSolarString = lines.find { it.split(",")[0] == id } ?: return null

        val sistemaSolarSplit = sistemaSolarString.split(",")
        // get planetas from an array of ids
        val planetas = sistemaSolarSplit[4].split(";")
            .map { PlanetasService.getInstance().getOne(it) }

        val validPlanetas = planetas.filterNotNull()

        return SistemaSolar(
            sistemaSolarSplit[0],
            sistemaSolarSplit[1],
            sistemaSolarSplit[2],
            sistemaSolarSplit[3].toDouble(),
            validPlanetas.toMutableList()
        )
    }

    fun getOneWithPlanetasWithoutSistemaSolar(id: String): SistemaSolar? {
        val lines = file.readLines()
        val sistemaSolarString = lines.find { it.split(",")[0] == id } ?: return null

        val sistemaSolarSplit = sistemaSolarString.split(",")
        // get planetas from an array of ids
        val planetas = sistemaSolarSplit[4].split(";")
            .map { PlanetasService.getInstance().getOneWithoutSistemaSolar(it) }

        val validPlanetas = planetas.filterNotNull()

        return SistemaSolar(
            sistemaSolarSplit[0],
            sistemaSolarSplit[1],
            sistemaSolarSplit[2],
            sistemaSolarSplit[3].toDouble(),
            validPlanetas.toMutableList()
        )
    }

    fun create(sistemaSolar: SistemaSolarDto): SistemaSolar {
        val newSistemaSolar = SistemaSolar(
            randomString(),
            sistemaSolar.name,
            sistemaSolar.description,
            sistemaSolar.price,
            sistemaSolar.planetas.toMutableList()
        )
        file.appendText(newSistemaSolar.toString() + "\n")
        return newSistemaSolar
    }

    fun update(sistemaSolar: SistemaSolar): SistemaSolar? {
        val lines = file.readLines()
        val sistemaSolarString = lines.find { it.split(",")[0] == sistemaSolar.getId() } ?: return null

        val sistemaSolarSplit = sistemaSolarString.split(",")

        val newSistemaSolar = SistemaSolar(
            sistemaSolarSplit[0],
            sistemaSolar.getName(),
            sistemaSolar.getDescription(),
            sistemaSolar.getPrice(),
            sistemaSolar.getPlanetas()
        )

        this.remove(sistemaSolar.getId())

        file.appendText(newSistemaSolar.toString() + "\n")

        return newSistemaSolar
    }

    fun remove(id: String) {
        file.readLines()
            .filter { it -> it.split(",")[0] != id }
            .joinToString("\n", postfix = "\n")
            .also { file.writeText(it) }
    }
}