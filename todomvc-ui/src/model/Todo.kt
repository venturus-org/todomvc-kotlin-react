package model

//TODO: Generate ids
data class Todo (
    //val id: Double = Date.now(),
    val title: String,
    var completed: Boolean = false
)