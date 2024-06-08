package models

class SistemaSolar(
    private val id: String,
    private val name: String,
    private val description: String,
    private val price: Double,
    private var planetas: MutableList<Planeta>,
) {

    constructor() : this("", "", "", 0.0, mutableListOf())

    public fun getId(): String {
        return id
    }

    fun getName(): String {
        return name
    }

    fun getDescription(): String {
        return description
    }

    fun getPrice(): Double {
        return price
    }

    fun getPlanetas(): MutableList<Planeta> {
        return planetas
    }

    fun addPlanetas(planetas: Planeta) {
        this.planetas.add(planetas)
    }

    fun removePlanetas(planetas: Planeta) {
        this.planetas = this.planetas.filter { it.getId() != planetas.getId() }.toMutableList()
    }

    public override fun toString(): String {
        val ids: String = planetas.map { it.getId() }.joinToString(";")
        return "$id,$name,$description,$price,$ids"
    }

    fun getListOfStringFromData(): List<String> {
        return listOf(
            "Nombre: $name",
            "Descripción: $description",
            "Edad: $price",
            "Planetas: ${planetas.map { it.getTitle() }}",
        )
    }
}