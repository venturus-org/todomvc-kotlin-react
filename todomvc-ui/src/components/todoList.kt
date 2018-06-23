package components

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import model.Todo
import model.TodoStatus
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import utils.value

class TodoList(props: Props): RComponent<TodoList.Props, RState>() {

    override fun RBuilder.render() {
        ul(classes = "todo-list") {
            props.todos.map { todo ->
                li(classes = todo.status.className) {
                    div(classes = "view") {
                        input(classes = "toggle", type = InputType.checkBox) {
                            this.attrs {
                                //TODO: Verify warning about uncontrolled components
                                checked = todo.status == TodoStatus.Completed
                                onChangeFunction = { event ->

                                    val isChecked = event.currentTarget.asDynamic().checked as Boolean

                                    val currentStatus = if (isChecked) {
                                        TodoStatus.Completed
                                    } else {
                                        TodoStatus.Pending
                                    }

                                    updateTodos(todo, todo.copy(status = currentStatus))
                                }
                            }
                        }
                        label { + todo.description }
                        button(classes = "destroy") {  }
                    }
                    input(classes = "edit", type = InputType.text) {
                        this.attrs {
                            value = todo.description
                            onChangeFunction = { event ->
                                updateTodos(todo, todo.copy(description = event.value))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateTodos(todo: Todo, updatedTodo: Todo) {
        //TODO: Review to check correctness.
        val todoIndex = props.todos.indexOf(todo)
        val updatedTodos : List<Todo> = props.todos.toMutableList().apply {
            this[todoIndex] = updatedTodo
        }

        props.updateList(updatedTodos)
    }

    class Props(var todos: Collection<Todo>, var updateList: (Collection<Todo>) -> Unit) : RProps
}

fun RBuilder.todoList(updateList: (Collection<Todo>) -> Unit, todos: Collection<Todo>) = child(TodoList::class) {
    attrs.todos = todos
    attrs.updateList = updateList
}