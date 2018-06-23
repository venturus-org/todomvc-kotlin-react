package utils

import app.AppOptions
import kotlin.js.Json
import kotlin.math.absoluteValue

object I18n {


    private val languageMap: Json by lazy {
        makeSyncRequest("GET", "/languages/" + AppOptions.language)!!
    }

    fun translate(key: String): String {
        return (languageMap[key] as String?) ?: "***$key"
    }

    fun pluralize(key: String): String {
        return (languageMap["PLURALS"].asDynamic()[key] as String?) ?: "***$key***"
    }
}

fun String.translate(): String {
    return I18n.translate(this)
}

fun String.pluralize(count: Int): String {
    return if (count.absoluteValue == 1) {
        this
    } else {
        I18n.pluralize(this)
    }
}