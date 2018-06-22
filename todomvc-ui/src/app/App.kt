package app

import headerInput.headerInput
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.title
import react.*
import react.dom.*
import model.Todo
import utils.translate

data class ApplicationOptions(
        val language: String
)

object AppOptions {
    var language = "no-language"
}

class App : RComponent<App.Props, App.State>() {
    override fun componentWillMount() {

        setState {
            todo = Todo()
        }

        AppOptions.apply {
            language = props.options.language
        }
    }

    override fun RBuilder.render() {
        section(classes = "todoapp") {
            headerInput(::updateTodo, state.todo)

            section(classes = "main") {
                input(classes = "toggle-all", type = InputType.checkBox){ this.attrs.id = "toggle-all"}
                label {
                    this.attrs["htmlFor"] = "toggle-all"
                    this.attrs.title = "Mark all as complete".translate()
                }
            }
        }

    }

    private fun updateTodo(newTodo: Todo) {
        setState {
            todo = newTodo
        }
    }

    class State(var todo: Todo) : RState
    class Props(var options: ApplicationOptions) : RProps

}

fun RBuilder.app(options: ApplicationOptions) = child(App::class) {
    attrs {
        this.options = options
    }
}

