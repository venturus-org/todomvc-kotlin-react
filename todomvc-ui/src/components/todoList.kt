package components

import kotlinx.html.InputType
import kotlinx.html.js.*
import model.Todo
import react.*
import react.dom.*
import utils.Keys
import utils.value

class TodoList(props: Props): RComponent<TodoList.Props, TodoList.State>() {

    override fun componentWillMount() {
        setState {
            editingText = ""
            editedTodo = null
        }
    }

    override fun componentWillUpdate(nextProps: Props, nextState: State) {
        if (state.editedTodo == null && nextState.editedTodo != null) {
            nextState.editingText = nextState.editedTodo?.title ?: ""
        }

        if(state.editedTodo != null && nextState.editedTodo == null) {
            nextState.editingText = ""
        }
    }

    override fun RBuilder.render() {
        ul(classes = "todo-list") {
            props.todos.map { todo ->

                val classes = when {
                    todo.completed -> "completed"
                    todo == state.editedTodo -> "editing"
                    else ->
                    ""
                }

                li(classes = classes) {
                    attrs {

                        onDoubleClickFunction = {
                            setState { editedTodo = todo }
                        }
                    }
                    div(classes = "view") {
                        input(classes = "toggle", type = InputType.checkBox) {
                            this.attrs {
                                //TODO: Verify warning about uncontrolled components
                                checked = todo.completed
                                onChangeFunction = { event ->
                                    val isChecked = event.currentTarget.asDynamic().checked as Boolean

                                    updateTodos(todo, todo.copy(completed = isChecked))
                                }
                            }
                        }
                        label { + todo.title }
                        button(classes = "destroy") {
                            attrs {
                                onClickFunction = { removeTodo(todo) }
                            }
                        }
                    }
                    input(classes = "edit", type = InputType.text) {
                        this.attrs {
                            value = state.editingText
                            onChangeFunction = { event ->
                                val text = event.value
                                setState { editingText = text }
                            }

                            onBlurFunction = { finishEditing(todo) }
                            onKeyDownFunction = { keyEvent ->
                                val key = Keys.fromString(keyEvent.asDynamic().key)
                                when(key) {
                                    Keys.Enter -> { finishEditing(todo) }
                                    Keys.Escape -> { endEditing() }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    private fun finishEditing(todo: Todo) {
        if(state.editingText.isBlank()) {
            removeTodo(todo)
        } else {
            updateTodos(todo, todo.copy(title = state.editingText))
        }

        endEditing()
    }

    private fun endEditing() {
        setState { editedTodo = null }
    }

    private fun removeTodo(todo: Todo) {
        val updatedTodos = props.todos.minus(todo)

        props.updateList(updatedTodos)
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
    class State(var editingText: String,var editedTodo: Todo?) : RState
}

fun RBuilder.todoList(updateList: (Collection<Todo>) -> Unit, todos: Collection<Todo>) = child(TodoList::class) {
    attrs.todos = todos
    attrs.updateList = updateList
}