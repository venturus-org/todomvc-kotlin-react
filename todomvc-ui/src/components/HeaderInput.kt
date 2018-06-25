package headerInput

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onKeyDownFunction
import model.Todo
import react.*
import react.dom.*
import utils.Keys
import utils.translate
import utils.value

class HeaderInput(props: Props): RComponent<HeaderInput.Props, HeaderInput.State>() {

    override fun RBuilder.render() {
        header(classes = "header") {
            h1 {
                +"todos".translate()
            }
            input(classes = "new-todo", type = InputType.text) {
                this.attrs {
                    autoFocus = true
                    placeholder = "What needs to be done?".translate()
                    value = state.title

                    onChangeFunction = { event ->
                        val newValue = event.value

                        setState {
                            title = newValue
                        }
                    }

                    onKeyDownFunction = { keyEvent ->
                        val key = Keys.fromString(keyEvent.asDynamic().key)
                        if (key == Keys.Enter) {
                            if (state.title.isNotBlank()) {
                                props.create(Todo(title = state.title))
                            }

                            setState {
                                title = ""
                            }
                        }
                    }
                }
            }
        }
    }

    class Props(var create: (Todo) -> Unit) : RProps
    class State(var title: String) : RState
}

fun RBuilder.headerInput(create: (Todo) -> Unit) = child(HeaderInput::class) {
    attrs.create = create
}