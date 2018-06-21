package utils

import org.w3c.dom.events.Event
import kotlin.js.Date

val Event.value: String
    get() = this.currentTarget.asDynamic().value as String

fun Date.toISO(): String {
    return this.toISOString().split("T")[0]
}

fun Date.toLocalString(): String {
    return this.toLocaleDateString(kotlin.browser.window.navigator.language)
}
