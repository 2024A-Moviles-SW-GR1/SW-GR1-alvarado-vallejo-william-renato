package models

import java.time.LocalDate
import java.util.Date

class Planeta(
    private val id: String,
    private val title: String,
    private val genre: String,
    private val isFinished: Boolean,
    private val seasons: Int,
    private val emissionDate: LocalDate,
    private val sistemaSolar: SistemaSolar,
) {

    constructor() : this("", "", "", false, 0, LocalDate.now(), SistemaSolar())

    public fun getId(): String {
        return id
    }

    public fun getTitle(): String {
        return title
    }

    fun getGenre(): String {
        return genre
    }

    fun getIsFinished(): Boolean {
        return isFinished
    }

    fun getSeasons(): Int {
        return seasons
    }

    fun getEmissionDate(): LocalDate {
        return emissionDate
    }

    fun getSistemaSolar(): SistemaSolar {
        return sistemaSolar
    }


    override fun toString(): String {
        return "$id,$title,$genre,$isFinished,$seasons,$emissionDate,${sistemaSolar.getId()}"
    }

    fun getListOfStringFromData(): List<String> {
        return listOf(
            "Nombre: $title",
            "Tipo: $genre",
            "Tiene Vida: $isFinished",
            "Fecha de Aparicion: $emissionDate",
            "Sistema Solar: ${sistemaSolar.getName()}",
        )
    }
}