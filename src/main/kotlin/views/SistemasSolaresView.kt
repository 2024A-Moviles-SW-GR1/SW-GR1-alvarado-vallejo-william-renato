package views

import dtos.SistemaSolarDto
import models.Planeta
import models.SistemaSolar
import services.SistemaSolarService

class SistemaSolarsView {

    private val tables = ConsoleTable()

    fun selectSistemaSolarsMenu(parent: MainView) {
        var goBack = false
        do {
            showSistemaSolarsMenu()
            val option = readln().toInt()
            when (option) {
                1 -> listSistemaSolars()
                2 -> createSistemaSolar()
                3 -> updateSistemaSolar()
                4 -> deleteSistemaSolar()
                5 -> {
                    goBack = true
                    parent.init()
                }
                else -> println("Opción no válida")
            }
        } while (!goBack)
    }

    fun showSistemaSolarsMenu() {
        println("Seleccione una opción:")
        println("1. Listar El Sistema Solar")
        println("2. Crear EL Sistema Solar")
        println("3. Actualizar El sistema Solar")
        println("4. Eliminar sistema solar")
        println("5. Volver atrás")
    }

    private fun listSistemaSolars() {
        val sistemaSolars = SistemaSolarService.getInstance().safeGetAll()
        if (sistemaSolars.isEmpty()) {
            println("No hay sistema Solar")
        } else {
            sistemaSolars.forEach { println(tables.createTableFromList(it.getListOfStringFromData())) }
        }
    }

    private fun createSistemaSolar() {
        println("Ingrese el nombre del sistema solar:")
        val name = readln()
        println("Ingrese la descripcion del sistema solar:")
        val description = readln()
        println("Ingrese la edad del sistema solar  :")
        val price = readln().toDouble()

        val sistemaSolar = SistemaSolarDto(
            name = name,
            description = description,
            price = price,
            planetas = listOf<Planeta>()
        )

        val createdSistemaSolar = SistemaSolarService.getInstance().create(sistemaSolar)
        val formattedData = tables.createTableFromList(createdSistemaSolar.getListOfStringFromData())
        println(formattedData)
        println("Sistema Solar creado con éxito")
    }

    private fun updateSistemaSolar() {
        val sistemaSolars = SistemaSolarService.getInstance().safeGetAll()
        if (sistemaSolars.isEmpty()) {
            println("No hay sistema solar ")
            return
        }
        sistemaSolars.forEachIndexed { index, it -> println("${index + 1}. ${it.getName()}") }
        println("Selecciona el sistema solar que deses editar:")
        val option = readln().toInt()
        if (option > sistemaSolars.size || option < 1) {
            println("Opción no válida")
            return
        }
        val selectedSistemaSolar = sistemaSolars[option - 1]
        println(tables.createTableFromList(selectedSistemaSolar.getListOfStringFromData()))
        println("Ingrese el nuevo nombre del sistema solar :")
        val name = readln()
        println("Ingrese la nueva descripción del sistema solar :")
        val description = readln()
        println("Ingrese la nueva edad del sistema solar:")
        val price = readln().toDouble()

        val sistemaSolar = SistemaSolar(
            id = selectedSistemaSolar.getId(),
            name = name,
            description = description,
            price = price,
            planetas = selectedSistemaSolar.getPlanetas()
        )

        val updatedSistemaSolar = SistemaSolarService.getInstance().update(sistemaSolar)
        if (updatedSistemaSolar == null) {
            println("No se pudo actualizar el sistema solar")
            return
        }
        val formattedData = tables.createTableFromList(updatedSistemaSolar.getListOfStringFromData())
        println(formattedData)
        println("Sistema Solar actualizado con éxito")
    }

    private fun deleteSistemaSolar() {
        val sistemaSolars = SistemaSolarService.getInstance().safeGetAll()
        if (sistemaSolars.isEmpty()) {
            println("No hay sistema solar")
            return
        }
        sistemaSolars.forEachIndexed { index, it -> println("${index + 1}. ${it.getName()}") }
        println("Selecciona el sistema solar que deseas eliminar:")
        val option = readln().toInt()
        if (option > sistemaSolars.size || option < 1) {
            println("Opción no válida")
            return
        }
        val selectedSistemaSolar = sistemaSolars[option - 1]
        if (selectedSistemaSolar.getPlanetas().isNotEmpty()) {
            println("No se puede eliminar el sistema solar porque tiene planetas asociados")
            return
        }
        val id = selectedSistemaSolar.getId()
        SistemaSolarService.getInstance().remove(id)
        println("Sistema Solar eliminado con éxito")
    }
}