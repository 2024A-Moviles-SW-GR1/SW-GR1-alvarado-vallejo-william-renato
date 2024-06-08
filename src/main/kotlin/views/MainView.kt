package views

import services.PlanetasService
import services.SistemaSolarService

class MainView {
    private val tables = ConsoleTable()
    private val planetasView = PlanetasView()
    private val sistemaSolarView = SistemaSolarsView()

    fun init() {
        header()
        selectEntitiesMenu()
    }

    fun header() {
        val tableWithTitle = tables.createTableWithText("Bienvenido a la Aplicación de Gestion Sistema Solar - Planetas")
        println(tableWithTitle)
    }

    fun selectEntitiesMenu() {
        var showAgain = false
        do {
            println("Seleccione una opción:")
            println("1. Planetas")
            println("2. Sistema Solar")
            println("3. Salir")

            val option = readln().toInt()
            when (option) {
                1 -> {
                    if (SistemaSolarService.getInstance().safeGetAll().isEmpty()) {
                        println("No existen un  Sistema Solare, primero crea uno.")
                        showAgain = true
                    } else {
                        planetasView.selectPlanetasMenu(this)
                    }
                }
                2 -> sistemaSolarView.selectSistemaSolarsMenu(this)
                3 -> println("Hasta pronto!")
                else -> println("Opción no válida")
            }
        } while (showAgain)

    }


}