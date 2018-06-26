package model

import kotlin.js.Date


data class Todo (
    val id: Double = guid(),
    val title: String,
    var completed: Boolean = false
)


fun guid(): Double {
    return Date().getTime()
}

