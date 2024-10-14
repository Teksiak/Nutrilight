package com.teksiak.nutrilight.core.domain

import java.util.Locale

enum class Country(
    val code: String
) {
    UNITED_KINGDOM("uk"),
    POLAND("pl"),
    GERMANY("de"),
    FRANCE("fr"),
    ITALY("it"),
    SPAIN("es");

    companion object {
        fun fromCode(code: String): Country = entries.first { it.code.contentEquals(code, true) }
        fun fromCodeOrNull(code: String): Country? = entries.firstOrNull { it.code.contentEquals(code, true) }
        fun fromLocale(locale: Locale): Country? {
            return when (locale.country) {
                "GB" -> UNITED_KINGDOM
                "PL" -> POLAND
                "DE" -> GERMANY
                "FR" -> FRANCE
                "IT" -> ITALY
                "ES" -> SPAIN
                else -> null
            }
        }
    }

}