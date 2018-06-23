package app

import components.info
import components.todoBar
import headerInput.headerInput
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.title
import react.*
import react.dom.*
import model.Todo
import components.todoList
import utils.pluralize
import utils.translate

data class ApplicationOptions(
        val language: String
)

object AppOptions {
    var language = "no-language"
}

class App : RComponent<App.Props, App.State>() {

    override fun componentWillMount() {
        setState {
            todo = Todo()
            todos = listOf()
        }

        AppOptions.apply {
            language = props.options.language
        }
    }

    override fun RBuilder.render() {
        section("todoapp") {
            headerInput(::createTodo, ::updateTodo, state.todo)

            if(state.todos.isNotEmpty()) {

                section("main") {
                    input(InputType.checkBox, classes = "toggle-all") { this.attrs.id = "toggle-all" }
                    label {
                        this.attrs["htmlFor"] = "toggle-all"
                        this.attrs.title = "Mark all as complete".translate()
                    }
                    todoList(::updateTodo, state.todos)
                }
                todoBar(state.todos.size, ::clearCompleted)

            }
        }
        info()
    }

    private fun createTodo(newTodo: Todo) {
        if (newTodo.description.trim().isNotEmpty()) {
            val trimmedTodo = newTodo.copy(newTodo.description.trim())
            
            setState {
                todo = Todo()
                todos = todos.plus(trimmedTodo)
            }
        }
    }

    private fun updateTodo(newTodo: Todo) {
        setState {
            todo = newTodo
        }
    }

    private fun clearCompleted() {

    }

    class State(var todo: Todo, var todos: Collection<Todo>) : RState
    class Props(var options: ApplicationOptions) : RProps

}

fun RBuilder.app(options: ApplicationOptions) = child(App::class) {
    attrs {
        this.options = options
    }
}

