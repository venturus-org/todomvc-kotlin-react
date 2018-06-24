package headerInput

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onKeyDownFunction
import model.Todo
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import utils.Keys
import utils.translate
import utils.value

class HeaderInput(props: Props): RComponent<HeaderInput.Props, RState>() {

    override fun RBuilder.render() {
        header(classes = "header") {
            h1 {
                +"todos".translate()
            }
            input(classes = "new-todo", type = InputType.text) {
                this.attrs {
                    autoFocus = true
                    placeholder = "What needs to be done?".translate()
                    value = props.todo.description
                    onChangeFunction = { event ->
                        props.update(props.todo.copy(description = event.value))
                    }
                    onKeyDownFunction = { keyEvent ->
                        val key = Keys.fromString(keyEvent.asDynamic().key)
                        if(key == Keys.Enter) {
                            props.create(props.todo)
                        }
                    }
                }
            }
        }
    }

    class Props(var todo: Todo,
                var update: (Todo) -> Unit,
                var create: (Todo) -> Unit) : RProps
}

fun RBuilder.headerInput(create: (Todo) -> Unit, update: (Todo) -> Unit, todo: Todo) = child(HeaderInput::class) {
    attrs.todo = todo
    attrs.update = update
    attrs.create = create
}