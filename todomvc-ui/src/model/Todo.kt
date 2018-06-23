package model

enum class TodoStatus(val className: String) {
    Pending("pending"),
    Editing("editing"),
    Completed("completed");

    companion object {
        fun fromBoolean(bool: Boolean) : TodoStatus {
            return if (bool) {
                TodoStatus.Completed
            } else {
                TodoStatus.Pending
            }
        }
    }
}

data class Todo (val description: String = "",var status: TodoStatus = TodoStatus.Pending)