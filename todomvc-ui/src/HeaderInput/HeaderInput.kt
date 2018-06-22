package headerInput

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import model.Todo
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import utils.translate
import utils.value

class HeaderInput(props: Props): RComponent<HeaderInput.Props, RState>() {

    override fun componentWillMount() {
        val todo = props.todo
    }

    override fun RBuilder.render() {
        header(classes = "header") {
            h1 {
                +"todos".translate()
            }
            input(classes = "new-todo") {
                this.attrs {
                    autoFocus = true
                    placeholder = "What needs to be done?".translate()
                    value = props.todo.description
                    type = InputType.text
                    onChangeFunction = { event ->
                        val value = event.value
                        props.update(props.todo.copy(description = value))
                    }
                }
            }
        }
    }

    class Props(var todo: Todo,
                var update: (Todo) -> Unit) : RProps
}

fun RBuilder.headerInput(update: (Todo) -> Unit, todo: Todo) = child(HeaderInput::class) {
    attrs.todo = todo
    attrs.update = update
}