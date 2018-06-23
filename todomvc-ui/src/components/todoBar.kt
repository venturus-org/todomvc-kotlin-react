package components

import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import utils.pluralize

class TodoBar(props: Props): RComponent<TodoBar.Props, RState>() {

    override fun RBuilder.render() {
        footer("footer") {
            span("todo-count") {
                strong { + props.count.toString() }
                + " "
                + "item left".pluralize(props.count)
            }
            button(classes = "clear-completed") {
                + "Clear completed"
                attrs {
                    onClickFunction = { props.clearCompleted() }
                }
            }
        }
    }

    class Props(var count: Int,
                var clearCompleted: () -> Unit) : RProps
}

fun RBuilder.todoBar(count: Int, clearCompleted: () -> Unit) = child(TodoBar::class) {
    attrs.count = count
    attrs.clearCompleted = clearCompleted
}