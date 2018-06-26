package components

import app.TodoFilter
import kotlinx.html.LI
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
                strong { + props.pendingCount.toString() }
                + " "
                + "item left".pluralize(props.pendingCount)
            }

            ul(classes = "filters") {
                li {
                    buildFilterItem(TodoFilter.ANY, "All")
                }
                span {}

                li {
                    buildFilterItem(TodoFilter.PENDING, "Active")
                }
                span {}
                li {
                    buildFilterItem(TodoFilter.COMPLETED, "Completed")

                }
                span {}
            }
            if (props.anyCompleted) {
                button(classes = "clear-completed") {
                    + "Clear completed"
                    attrs {
                        onClickFunction = {
                            props.clearCompleted()
                        }
                    }
                }
            }
        }
    }

    private fun RDOMBuilder<LI>.buildFilterItem(filter: TodoFilter, text: String) {
        a(classes = if (props.currentFilter == filter) {
            "selected"
        } else {
            ""
        }) {
            attrs {
                href = "#"
                onClickFunction = {
                    props.updateFilter(filter)
                }
            }
            +text
        }
    }

    class Props(var pendingCount: Int,
                var anyCompleted: Boolean,
                var clearCompleted: () -> Unit,
                var updateFilter: (TodoFilter) -> Unit,
                var currentFilter: TodoFilter) : RProps
}

fun RBuilder.todoBar(pendingCount: Int, anyCompleted: Boolean, clearCompleted: () -> Unit, currentFilter: TodoFilter, updateFilter: (TodoFilter) -> Unit) = child(TodoBar::class) {
    attrs.pendingCount = pendingCount
    attrs.clearCompleted = clearCompleted
    attrs.anyCompleted = anyCompleted
    attrs.updateFilter = updateFilter
    attrs.currentFilter = currentFilter
}