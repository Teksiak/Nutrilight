package com.teksiak.nutrilight.more

sealed interface MoreAction {
    data object ToggleProductImages : MoreAction
    data object ShowCountrySelect: MoreAction
    data class SelectCountry(val code: String): MoreAction
    data object HideCountrySelect: MoreAction
    data object ToggleCountryExplanation: MoreAction
}