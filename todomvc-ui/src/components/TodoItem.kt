package components

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import model.Todo
import react.*
import react.dom.button
import react.dom.div
import react.dom.input
import react.dom.label

class TodoItem : RComponent<TodoItem.Props, TodoItem.State>() {

    init {
        setState {
            editText = props.todo.title
        }
    }

    override fun RBuilder.render() {
        val todo = props.todo

        div(classes = "view") {
            input(classes = "toggle", type = InputType.checkBox) {
                this.attrs {
                    checked = todo.completed
                    onChangeFunction = { event ->
                        val checked = event.currentTarget.asDynamic().checked as Boolean

                        props.updateTodo(todo.copy(completed = checked))
                    }
                }
            }
            label { +todo.title }
            button(classes = "destroy") {
//                attrs {
//                    onClickFunction = { props.removeTodo(todo) }
//                }
            }

            input(classes = "edit", type = InputType.text) {
                this.attrs {
                    value = state.editText
//                    onChangeFunction = { event ->

//                        val text = event.value
//                        setState {
//                            editText = text
//                        }
//                    }
//                    onBlurFunction = { event ->
//                        val title = state.editText
//                        finishEditing(title, todo)
//                    }
//                    onKeyDownFunction = ::handleKeyDown
                }
            }
        }


    }

    private fun finishEditing(title: String, todo: Todo) {
//        if (title.isNotBlank()) {
//            props.updateTodo(todo.copy(title = title))
//        } else {
//            props.removeTodo(todo)
//        }
//
//        props.endEditing()
    }

//    private fun handleKeyDown(keyEvent: Event) {
//        val key = Keys.fromString(keyEvent.asDynamic().key)
//        when (key) {
//            Keys.Enter -> {
//                finishEditing(state.editText, props.todo)
//            }
//            Keys.Escape -> {
//                props.endEditing()
//            }
//        }
//
//    }

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