package utils

import app.AppOptions
import kotlin.js.Json

object I18n {


    private val languageMap: Json by lazy {
        makeSyncRequest("GET", "/languages/" + AppOptions.language)!!
    }

    fun translate(key: String): String {
        return (languageMap[key] as String?) ?: "***$key"
    }

}

fun String.translate(): String {
    return I18n.translate(this)
}