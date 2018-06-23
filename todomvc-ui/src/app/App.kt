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
import kotlinx.html.js.onChangeFunction
import model.TodoStatus
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
                val allChecked = isAllCompleted()
                val someButNotAllChecked = !allChecked && isAnyCompleted()

                section("main") {
                    input(InputType.checkBox, classes = "toggle-all") {
                        this.attrs {
                            id = "toggle-all"
                            checked = allChecked
                            //TODO: Review if there is 'indetermitate' support
                            //https://github.com/facebook/react/issues/1798
                            ref { it?.indeterminate = someButNotAllChecked }
                            onChangeFunction = {event ->
                                val isChecked = event.currentTarget.asDynamic().checked as Boolean
                                val status = TodoStatus.fromBoolean(isChecked)

                                setAllStatus(status)
                            }
                        }
                    }
                    label {
                        this.attrs["htmlFor"] = "toggle-all"
                        this.attrs.title = "Mark all as complete".translate()
                    }
                    todoList(::updateTodos, state.todos)
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

    private fun updateTodo(updatedTodo: Todo) {
        setState {
            todo = updatedTodo
        }
    }

    private fun updateTodos(updatedTodos: Collection<Todo>) {
        setState {
            todos = updatedTodos
        }
    }

    private fun clearCompleted() {

    }

    private fun isAllCompleted(): Boolean {
        return state.todos.fold(true) { allCompleted, todo ->
            allCompleted && (todo.status == model.TodoStatus.Completed)
        }
    }

    private fun isAnyCompleted(): Boolean {
        return state.todos.any { todo ->
            todo.status == model.TodoStatus.Completed
        }
    }

    private fun setAllStatus(newStatus: TodoStatus) {
        setState {
            todos = todos.map { todo -> todo.copy(status = newStatus)  }
        }
    }

    class State(var todo: Todo, var todos: Collection<Todo>) : RState
    class Props(var options: ApplicationOptions) : RProps

}

fun RBuilder.app(options: ApplicationOptions) = child(App::class) {
    attrs {
        this.options = options
    }
}

