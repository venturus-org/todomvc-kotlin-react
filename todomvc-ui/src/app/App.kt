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
import org.w3c.dom.get
import org.w3c.dom.set
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

                    todoList(::removeTodo, ::updateTodo, state.todos)
                }
                todoBar(pendingTodos().size, state.todos.any {todo -> todo.completed }, ::clearCompleted)
            }

        }
        info()
    }

    private fun removeTodo(todo: Todo) {
        saveTodos(state.todos - todo)
    }

    private fun createTodo(todo: Todo) {
        saveTodos(state.todos + todo)
    }

    private fun saveTodos(updatedTodos: List<Todo>) {

        console.log("saving: ${updatedTodos.toTypedArray()}")
        setState {
            todos = updatedTodos
        }
        storeTodos(updatedTodos)
    }

    private fun updateTodo(todo: Todo) {
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

