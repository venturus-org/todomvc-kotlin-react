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
import org.w3c.dom.get
import org.w3c.dom.set
import utils.translate
import kotlin.browser.localStorage

data class ApplicationOptions(
        val language: String
)

object AppOptions {
    var language = "no-language"
    var localStorageKey = "todos-koltin-react"
}

class App : RComponent<App.Props, App.State>() {

    override fun componentWillMount() {

        setState {
            todos = loadTodos()
        }

        AppOptions.apply {
            language = props.options.language
        }
    }

    private fun loadTodos(): List<Todo> {
        val storedTodosJSON = localStorage[AppOptions.localStorageKey]

        return if (storedTodosJSON != null) {
            JSON.parse<Array<Todo>>(storedTodosJSON).map {
                Todo(it.title, it.completed)
            }.toList()
        } else {
            emptyList()
        }
    }

    override fun RBuilder.render() {
        section("todoapp") {
            headerInput(::createTodo)

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

                                setAllStatus(isChecked)
                            }
                        }
                    }
                    label {
                        this.attrs["htmlFor"] = "toggle-all"
                        this.attrs.title = "Mark all as complete".translate()
                    }

                    todoList(::removeTodo, ::updateTodo, state.todos)
                }
                todoBar(pendingTodos().size, state.todos.any {todo -> todo.completed }, ::clearCompleted)
            }
        }
        info()
    }

    private fun removeTodo(todo: Todo) {
        setState {
            todos = todos.minus(todo)
        }

        storeTodos()
    }

    private fun updateTodo(todo: Todo) {
        setState {
            val todoIndex = todos.indexOf(todo)

            todos = todos.toMutableList().apply {
                this[todoIndex] = todo
            }
        }

        storeTodos()
    }

    private fun createTodo(todo: Todo) {
        setState {
            todos = state.todos.plus(todo)
        }

        storeTodos()
    }

    private fun storeTodos() {
        localStorage.set(AppOptions.localStorageKey, JSON.stringify(state.todos.toTypedArray()))
    }

    private fun clearCompleted() {
        setState {
            todos = pendingTodos()
        }

        storeTodos()
    }

    private fun isAllCompleted(): Boolean {
        return state.todos.fold(true) { allCompleted, todo ->
            allCompleted && todo.completed
        }
    }

    private fun isAnyCompleted(): Boolean {
        return state.todos.any { todo ->  todo.completed }
    }

    private fun setAllStatus(newStatus: Boolean) {
        setState {
            todos = todos.map { todo -> todo.copy(completed = newStatus)  }
        }
    }

    private fun pendingTodos() : List<Todo> {
        return state.todos.filter { todo -> !todo.completed }
    }

    class State(var title: String,
                var todos: List<Todo>) : RState
    class Props(var options: ApplicationOptions) : RProps

}

fun RBuilder.app(options: ApplicationOptions) = child(App::class) {
    attrs {
        this.options = options
    }
}

