package utils

import org.w3c.dom.events.Event
import kotlin.js.Date

val Event.value: String
    get() = this.currentTarget.asDynamic().value as String

val Event.rawValue: Any?
    get() = {
        val value = this.currentTarget.asDynamic().value

        when (jsTypeOf(value)) {
            "string" -> value as String
            "number" -> value as Float
            "boolean" -> value as Boolean
            "null" -> null
            "undefined" -> null

            else -> value
        }
    }

fun Date.toISO(): String {
    return this.toISOString().split("T")[0]
}

fun Date.toLocalString(): String {
    return this.toLocaleDateString(kotlin.browser.window.navigator.language)
}
