package components

import kotlinx.html.js.onDoubleClickFunction
import model.Todo
import react.*
import react.dom.li
import react.dom.ul

class TodoList : RComponent<TodoList.Props, TodoList.State>() {

    init {
        setState {
            editingIdx = -1
        }
    }

    override fun RBuilder.render() {
        ul(classes = "todo-list") {
            props.todos.mapIndexed { idx, todo ->

                val isEditing = idx == state.editingIdx

                val classes = when {
                    todo.completed -> "completed"
                    isEditing -> "editing"
                    else -> ""
                }

                li(classes = classes) {
                    attrs {
                        onDoubleClickFunction = {
                            setState {
                                editingIdx = idx
                            }
                        }
                    }

                    todoItem(todo, isEditing, props.updateTodo, props.removeTodo, {
                        setState {
                            editingIdx = -1
                        }
                    })
                }
            }
        }
    }

    class Props(var removeTodo: (Todo) -> Unit, var updateTodo: (Todo) -> Unit, var todos: List<Todo>) : RProps
    class State(var editingIdx: Int) : RState
}

fun RBuilder.todoList(removeTodo: (Todo) -> Unit, updateTodo: (Todo) -> Unit, todos: List<Todo>) = child(TodoList::class) {
    attrs.todos = todos
    attrs.removeTodo = removeTodo
    attrs.updateTodo = updateTodo
}