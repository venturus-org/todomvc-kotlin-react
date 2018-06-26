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
            editText = props.title
        }
    }

    override fun componentWillReceiveProps(nextProps: Props) {
        setState {
            editText = nextProps.title
        }
    }

    override fun RBuilder.render() {

        console.log("${props.title} ${props.completed}")

        div(classes = "view") {
            input(classes = "toggle", type = InputType.checkBox) {

                attrs {
                    checked = props.completed

                    onChangeFunction = {
                        props.updateTodo(props.title, !props.completed)
                    }
                }
            }
            label {
                +props.title
            }
            button(classes = "destroy") {
                attrs {
                    onClickFunction = {
                        props.removeTodo()
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
                onBlurFunction = { finishEditing(state.editText) }
                onKeyUpFunction = ::handleKeyUp

            }

            if (props.editing) {
                ref { it ->
                    it?.focus()
                }
            }
        }
    }

    private fun finishEditing(title: String) {
        if (title.isNotBlank()) {
            props.updateTodo(title, props.completed)
        } else {
            props.removeTodo()
        }

        props.endEditing()
    }

    private fun handleKeyUp(keyEvent: Event) {
        val key = Keys.fromString(keyEvent.asDynamic().key as String)
        when (key) {
            Keys.Enter -> {
                finishEditing(state.editText)
            }
            Keys.Escape -> {
                props.endEditing()
            }
        }

    }

    class Props(var title: String,var completed: Boolean, var editing: Boolean, var removeTodo: () -> Unit, var updateTodo: (String, Boolean) -> Unit, var endEditing: () -> Unit) : RProps
    class State(var editText: String) : RState
}

fun RBuilder.todoItem(title: String, completed: Boolean, editing: Boolean, removeTodo: () -> Unit, updateTodo: (String, Boolean) -> Unit, endEditing: () -> Unit) = child(TodoItem::class) {
    attrs.title = title
    attrs.completed = completed
    attrs.editing = editing
    attrs.removeTodo = removeTodo
    attrs.updateTodo = updateTodo
    attrs.endEditing = endEditing
}