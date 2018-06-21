package index

import app.*
import kotlinext.js.*
import react.dom.*
import kotlin.browser.*

fun main(args: Array<String>) {
    requireAll(require.context("src", true, js("/\\.css$/")))

    val options = ApplicationOptions(language = "language-en_US.json")

    render(document.getElementById("root")) {
        app(options)
    }
}
