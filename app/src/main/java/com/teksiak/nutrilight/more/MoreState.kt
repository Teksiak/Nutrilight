package com.teksiak.nutrilight.more

import com.teksiak.nutrilight.core.domain.Country

data class MoreState(
    val selectedCountry: Country = Country.POLAND,
    val showCountrySelectDialog: Boolean = false,
    val showCountryExplanation: Boolean = false,
    val showProductImages: Boolean = true,
    val areSettingsLoaded: Boolean = false
)
