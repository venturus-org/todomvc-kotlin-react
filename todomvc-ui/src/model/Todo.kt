package model

enum class TodoStatus(val className: String) {
    Pending("pending"),
    Editing("editing"),
    Completed("completed")
}

data class Todo (val description: String = "",var status: TodoStatus = TodoStatus.Pending)