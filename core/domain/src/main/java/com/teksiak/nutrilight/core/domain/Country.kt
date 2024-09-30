package com.teksiak.nutrilight.core.domain

enum class Country(
    val code: String
) {
    POLAND("pl"),
    GERMANY("de"),
    FRANCE("fr"),
    ITALY("it"),
    SPAIN("es");

    companion object {
        fun fromCode(code: String): Country = entries.first { it.code == code }
    }

}