package components

import kotlinx.html.InputType
import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyUpFunction
import model.Todo
import org.w3c.dom.events.Event
import react.*
import react.dom.button
import react.dom.div
import react.dom.input
import react.dom.label
import utils.Keys
import utils.value

class TodoItem : RComponent<TodoItem.Props, TodoItem.State>() {

    override fun componentWillMount() {
        setState {
            editText = props.todo.title
        }
    }

    override fun componentWillReceiveProps(nextProps: Props) {
        setState {
            editText = nextProps.todo.title
        }
    }

    override fun RBuilder.render() {

        console.log("${props.todo.title} ${props.todo.completed}")

        div(classes = "view") {
            input(classes = "toggle", type = InputType.checkBox) {

                attrs {
                    checked = props.todo.completed

                    onChangeFunction = {
                        props.updateTodo(props.todo.copy(completed = !props.todo.completed))
                    }
                }
            }
            label {
                +props.todo.title
            }
            button(classes = "destroy") {
                attrs {
                    onClickFunction = {
                        props.removeTodo(props.todo)
                    }
                }
            }
        }
        input(classes = "edit", type = InputType.text) {
            attrs {
                value = state.editText
                onChangeFunction = { event ->
                    val text = event.value
                    setState {
                        editText = text
                    }
                }
                onBlurFunction = { finishEditing(state.editText, props.todo) }
                onKeyUpFunction = ::handleKeyUp

            }

            if (props.editing) {
                ref { it ->
                    it?.focus()
                }
            }
        }
    }

    private fun finishEditing(title: String, todo: Todo) {
        if (title.isNotBlank()) {
            props.updateTodo(todo.copy(title = title))
        } else {
            props.removeTodo(todo)
        }

        props.endEditing()
    }

    private fun handleKeyUp(keyEvent: Event) {
        val key = Keys.fromString(keyEvent.asDynamic().key as String)
        when (key) {
            Keys.Enter -> {
                finishEditing(state.editText, props.todo)
            }
            Keys.Escape -> {
                props.endEditing()
            }
        }

    }

    class Props(var todo: Todo, var editing: Boolean, var removeTodo: (Todo) -> Unit, var updateTodo: (Todo) -> Unit, var endEditing: () -> Unit) : RProps
    class State(var editText: String) : RState
}

fun RBuilder.todoItem(todo: Todo, editing: Boolean, removeTodo: (Todo) -> Unit, updateTodo: (Todo) -> Unit, endEditing: () -> Unit) = child(TodoItem::class) {
    attrs.todo = todo
    attrs.editing = editing
    attrs.removeTodo = removeTodo
    attrs.updateTodo = updateTodo
    attrs.endEditing = endEditing
}