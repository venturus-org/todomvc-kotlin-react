package todoList

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import model.Todo
import model.TodoStatus
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import utils.rawValue
import utils.value

class TodoList(props: Props): RComponent<TodoList.Props, RState>() {

    override fun RBuilder.render() {
        ul(classes = "todo-list") {
            props.todos.map { todo ->
                li(classes = todo.status.className) {
                    div(classes = "view") {
                        input(classes = "toggle", type = InputType.checkBox) {
                            this.attrs {
                                checked = todo.status == TodoStatus.Completed
                                onChangeFunction = { event ->

                                    val currentStatus = if (event.rawValue as Boolean) {
                                        TodoStatus.Completed
                                    } else {
                                        TodoStatus.Pending
                                    }

                                    props.update(todo.copy(status = currentStatus))
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
                                props.update(todo.copy(description = event.value))
                            }
                        }
                    }
                }
            }
        }
    }

    class Props(var todos: Collection<Todo>, var update: (Todo) -> Unit) : RProps
}

fun RBuilder.todoList(update: (Todo) -> Unit, todos: Collection<Todo>) = child(TodoList::class) {
    attrs.todos = todos
    attrs.update = update
}