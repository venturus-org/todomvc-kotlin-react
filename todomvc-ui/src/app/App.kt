package app

import components.info
import components.headerInput
import components.todoBar
import kotlinx.html.InputType
import kotlinx.html.id
import react.*
import react.dom.*
import model.Todo
import components.todoList
import kotlinx.html.js.onChangeFunction
import kotlinx.html.title
import org.w3c.dom.get
import org.w3c.dom.set
import utils.translate
import kotlin.browser.localStorage

data class ApplicationOptions(
        val language: String
)

enum class TodoFilter {
    ANY, COMPLETED, PENDING
}

object AppOptions {
    var language = "no-language"
    var localStorageKey = "todos-koltin-react"
}

class App : RComponent<App.Props, App.State>() {

    override fun componentWillMount() {
        console.log("component will mount app")
        setState {
            todos = loadTodos()
            filter = TodoFilter.ANY
        }

        AppOptions.apply {
            language = props.options.language
        }
    }

    private fun loadTodos(): List<Todo> {
        val storedTodosJSON = localStorage[AppOptions.localStorageKey]

        return if (storedTodosJSON != null) {
            JSON.parse<Array<Todo>>(storedTodosJSON).map {
                Todo(it.id, it.title, it.completed)
            }.toList()
        } else {
            emptyList()
        }
    }

    override fun RBuilder.render() {
        section("todoapp") {
            headerInput(::createTodo)


            if (state.todos.isNotEmpty()) {

                val allChecked = isAllCompleted()
                val someButNotAllChecked = !allChecked && isAnyCompleted()

                section("main") {
                    input(InputType.checkBox, classes = "toggle-all") {
                        attrs {
                            id = "toggle-all"
                            checked = allChecked

                            onChangeFunction = {event ->
                                val isChecked = event.currentTarget.asDynamic().checked as Boolean

                                setAllStatus(isChecked)
                            }
                        }
                        ref { it?.indeterminate = someButNotAllChecked }
                    }
                    label {
                        attrs["htmlFor"] = "toggle-all"
                        attrs.title = "Mark all as complete".translate()
                    }



                    todoList(::removeTodo, ::updateTodo, state.todos, state.filter)
                }
                todoBar(pendingCount = countPending(),
                        anyCompleted = containsAnyComplete(),
                        clearCompleted = ::clearCompleted,
                        currentFilter = state.filter,
                        updateFilter = ::updateFilter)
            }

        }
        info()
    }




    private fun updateFilter(newFilter: TodoFilter) {
        setState {
            filter = newFilter
        }
    }

    private fun countPending() = pendingTodos().size

    private fun removeTodo(todo: Todo) {
        console.log("removeTodo [${todo.id}] ${todo.title}")
        saveTodos(state.todos - todo)
    }

    private fun createTodo(todo: Todo) {
        console.log("createTodo [${todo.id}] ${todo.title}")
        saveTodos(state.todos + todo)
    }

    private fun saveTodos(updatedTodos: List<Todo>) {
        console.log("saving: ${updatedTodos.toTypedArray()}")

        storeTodos(updatedTodos)

        setState {
            todos = updatedTodos
        }
    }

    private fun updateTodo(todo: Todo) {
        console.log("updateTodo [${todo.id}] ${todo.title}")

        saveTodos(state.todos.map { oldTodo ->
            if (todo.id == oldTodo.id) {
                todo
            } else {
                oldTodo
            }
        })
    }



    private fun storeTodos(todos: List<Todo>) {
        localStorage[AppOptions.localStorageKey] = JSON.stringify(todos.toTypedArray())
    }

    private fun clearCompleted() {
        saveTodos(pendingTodos())
    }

    private fun isAllCompleted(): Boolean {
        return state.todos.fold(true) { allCompleted, todo ->
            allCompleted && todo.completed
        }
    }

    private fun isAnyCompleted(): Boolean {
        return containsAnyComplete()
    }

    private fun containsAnyComplete() = state.todos.any { todo -> todo.completed }

    private fun setAllStatus(newStatus: Boolean) {
        setState {
            todos = todos.map { todo -> todo.copy(completed = newStatus)  }
        }
    }

    private fun pendingTodos() : List<Todo> {
        return state.todos.filter { todo -> !todo.completed }
    }

    class State(var todos: List<Todo>,
                var filter: TodoFilter) : RState
    class Props(var options: ApplicationOptions) : RProps

}

fun RBuilder.app(options: ApplicationOptions) = child(App::class) {
    attrs {
        this.options = options
    }
}

