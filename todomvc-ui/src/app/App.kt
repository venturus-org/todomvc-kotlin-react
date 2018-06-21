package app

import HeaderInput.headerInput
import react.*
import react.dom.*
import model.Todo

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

