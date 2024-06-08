package dtos

import models.SistemaSolar
import java.time.LocalDate

class PlanetasDto (
    val title: String,
    val genre: String,
    val isFinished: Boolean,
    val seasons: Int,
    val emissionDate: LocalDate,
    val sistemaSolar: SistemaSolar,
){
}