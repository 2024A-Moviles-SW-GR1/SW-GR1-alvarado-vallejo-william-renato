package dtos

import models.Planeta

class SistemaSolarDto(
    val name: String,
    val description: String,
    val price: Double,
    val planetas: List<Planeta>,
) {
}