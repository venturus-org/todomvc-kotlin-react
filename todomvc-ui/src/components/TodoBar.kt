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
            span("todo-pendingCount") {
                strong { + props.pendingCount.toString() }
                + " "
                + "item left".pluralize(props.pendingCount)
            }
            if(props.anyCompleted) {
                button(classes = "clear-completed") {
                    + "Clear completed"
                    attrs {
                        onClickFunction = { props.clearCompleted() }
                    }
                }
            }
        }
    }

    class Props(var pendingCount: Int,
                var anyCompleted: Boolean,
                var clearCompleted: () -> Unit) : RProps
}

fun RBuilder.todoBar(pendingCount: Int, anyCompleted: Boolean, clearCompleted: () -> Unit) = child(TodoBar::class) {
    attrs.pendingCount = pendingCount
    attrs.clearCompleted = clearCompleted
    attrs.anyCompleted = anyCompleted
}