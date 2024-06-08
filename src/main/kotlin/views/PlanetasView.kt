package views

import dtos.PlanetasDto
import models.Planeta
import services.PlanetasService
import services.SistemaSolarService
import java.time.LocalDate

class PlanetasView {

    val tables = ConsoleTable()

    fun selectPlanetasMenu(parent: MainView) {
        var goBack = false
        do {
            println("Seleccione una opción:")
            println("1. Listar planetas")
            println("2. Crear Planeta")
            println("3. Actualizar Planeta")
            println("4. Eliminar Planeta")
            println("5. Volver atrás")

            val option = readln().toInt()
            when (option) {
                1 -> listAllPlanetas()
                2 -> createPlaneta()
                3 -> updatePlaneta()
                4 -> deletePlaneta()
                5 -> {
                    goBack = true
                    parent.init()
                }
                else -> println("Opción no válida")
            }
        } while (!goBack)
    }

    fun listAllPlanetas() {
        val planetas: List<Planeta> = PlanetasService.getInstance().safeGetAll()

        if (planetas.isEmpty()) {
            println("No hay planetas")
        } else {
            planetas.forEach { println(tables.createTableFromList(it.getListOfStringFromData())) }
        }
    }

    fun createPlaneta() {
        println("Ingrese el nombre del planeta:")
        val title = readln()
        println("Ingrese el tipo de planeta que es (rocoso/terrestre)")
        val genre = readln()
        println("¿Tiene vida? (s/n)")
        val isFinished = readln().lowercase() == "s"
        println("Ingrese el número de orbitas del planeta:")
        val seasons = readln().toInt()
        println("Ingrese la fecha de aparicion del planeta (formato: yyyy-MM-dd):")
        val emissionDate = LocalDate.parse(readln())
        println("Selecciona el sistema solar del planeta al que pertenece:")
        val sistemaSolars = SistemaSolarService.getInstance().safeGetAll()
        sistemaSolars.forEachIndexed { index, sistemaSolar ->
            println("${index + 1}. ${sistemaSolar.getName()}")
        }
        val sistemaSolarIndex = readln().toInt() - 1
        val sistemaSolar = sistemaSolars[sistemaSolarIndex]

        val planetas = PlanetasDto(
            title = title,
            genre = genre,
            isFinished = isFinished,
            seasons = seasons,
            emissionDate = emissionDate,
            sistemaSolar = sistemaSolar,
        )

        val createdPlanetas = PlanetasService.getInstance().create(planetas)
        sistemaSolar.addPlanetas(createdPlanetas)
        SistemaSolarService.getInstance().update(sistemaSolar)
        val formattedData = tables.createTableFromList(createdPlanetas.getListOfStringFromData())
        println(formattedData)
        println("Planeta creado correctamente")
    }

    fun updatePlaneta() {
        val planetas = PlanetasService.getInstance().safeGetAll()
        if (planetas.isEmpty()) {
            println("No hay planetas")
            return
        }
        planetas.forEachIndexed { index, it -> println("${index + 1}. ${it.getTitle()}") }
        println("Selecciona un planeta que deseas actualizar:")
        val option = readln().toInt()
        if (option > planetas.size || option < 1) {
            println("Opción no válida")
            return
        }
        val selectedPlanetas = planetas[option - 1]

        println(tables.createTableFromList(selectedPlanetas.getListOfStringFromData()))

        println("Ingrese el nombre del planeta:")
        val title = readln()
        println("Ingrese el tipo de planeta que es (rocoso/terrestre)")
        val genre = readln()
        println("¿Tiene vida? (s/n)")
        val isFinished = readln().lowercase() == "s"
        println("Ingrese el número de orbitas del planeta:")
        val seasons = readln().toInt()
        println("Ingrese la fecha de aparicion del planeta (formato: yyyy-MM-dd):")
        val emissionDate = LocalDate.parse(readln())
        println("Selecciona el sistema solar del planeta al que pertenece:")
        val sistemaSolars = SistemaSolarService.getInstance().safeGetAll()
        sistemaSolars.forEachIndexed { index, sistemaSolar ->
            println("${index + 1}. ${sistemaSolar.getName()}")
        }
        val sistemaSolarIndex = readln().toInt()

        if (sistemaSolarIndex > sistemaSolars.size || sistemaSolarIndex < 1) {
            println("Opción no válida")
            return
        }

        val sistemaSolar = sistemaSolars[sistemaSolarIndex - 1]

        val updatedPlanetas = Planeta(
            id = selectedPlanetas.getId(),
            title = title,
            genre = genre,
            isFinished = isFinished,
            seasons = seasons,
            emissionDate = emissionDate,
            sistemaSolar = sistemaSolar,
        )

        val savedPlaneta = PlanetasService.getInstance().update(updatedPlanetas)

        if (savedPlaneta == null) {
            println("No se pudo actualizar el planeta")
            return
        }

        sistemaSolar.removePlanetas(selectedPlanetas)
        sistemaSolar.addPlanetas(savedPlaneta)
        SistemaSolarService.getInstance().update(sistemaSolar)

        val formattedData = tables.createTableFromList(updatedPlanetas.getListOfStringFromData())
        println(formattedData)
        println("Planeta actualizado correctamente")
    }

    fun deletePlaneta() {
        val planetas = PlanetasService.getInstance().safeGetAll()
        if (planetas.isEmpty()) {
            println("No hay planetas")
            return
        }
        planetas.forEachIndexed { index, it -> println("${index + 1}. ${it.getTitle()}") }
        println("Selecciona el planeta que deseas eliminar:")
        val option = readln().toInt()
        if (option > planetas.size || option < 1) {
            println("Opción no válida")
            return
        }
        val selectedPlanetas = planetas[option - 1]
        val sistemaSolar = selectedPlanetas.getSistemaSolar()
        sistemaSolar.removePlanetas(selectedPlanetas)
        SistemaSolarService.getInstance().update(sistemaSolar)
        PlanetasService.getInstance().remove(selectedPlanetas.getId())
        println("Planeta eliminado con éxito")
    }
}