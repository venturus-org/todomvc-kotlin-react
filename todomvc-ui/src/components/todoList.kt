package components

import kotlinx.html.InputType
import kotlinx.html.js.*
import model.Todo
import model.TodoStatus
import react.*
import react.dom.*
import utils.Keys
import utils.value

class TodoList(props: Props): RComponent<TodoList.Props, TodoList.State>() {

    override fun componentWillMount() {
        setState {
            editingText = ""
            isEditing = false
        }
    }

    override fun componentWillUpdate(nextProps: Props, nextState: State) {
        val editingTodo = nextProps.todos.find { todo ->
            todo.status == TodoStatus.Editing
        }

        if(!this.state.isEditing && editingTodo != null) {
            nextState.isEditing = true
            nextState.editingText = editingTodo.description
        }

        if(this.state.isEditing && editingTodo == null) {
            nextState.isEditing = false
        }
    }

    override fun RBuilder.render() {
        ul(classes = "todo-list") {
            props.todos.map { todo ->
                li(classes = todo.status.className) {
                    attrs {
                        onDoubleClickFunction = {
                            updateTodos(todo, todo.copy(status = TodoStatus.Editing))
                        }
                    }
                    div(classes = "view") {
                        input(classes = "toggle", type = InputType.checkBox) {
                            this.attrs {
                                //TODO: Verify warning about uncontrolled components
                                checked = todo.status == TodoStatus.Completed
                                onChangeFunction = { event ->
                                    val isChecked = event.currentTarget.asDynamic().checked as Boolean
                                    val currentStatus = TodoStatus.fromBoolean(isChecked)

                                    updateTodos(todo, todo.copy(status = currentStatus))
                                }
                            }
                        }
                        label { + todo.description }
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
                                    Keys.Escape -> { cancelEditing(todo) }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    //TODO: Review if todo should restore it's previous status or become 'pending'
    private fun finishEditing(todo: Todo) {
        if(state.editingText.isBlank()) {
            removeTodo(todo)
        } else {
            updateTodos(todo, todo.copy(description = state.editingText, status = model.TodoStatus.Pending))
        }
    }

    //TODO: Review if todo should restore it's previous status or become 'pending'
    private fun cancelEditing(todo: Todo) {
        updateTodos(todo, todo.copy(status = model.TodoStatus.Pending))
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
    class State(var editingText: String,var isEditing: Boolean) : RState
}

fun RBuilder.todoList(updateList: (Collection<Todo>) -> Unit, todos: Collection<Todo>) = child(TodoList::class) {
    attrs.todos = todos
    attrs.updateList = updateList
}