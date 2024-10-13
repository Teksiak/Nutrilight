package com.teksiak.nutrilight.more.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.teksiak.nutrilight.R
import com.teksiak.nutrilight.core.domain.Country
import com.teksiak.nutrilight.core.presentation.designsystem.FranceFlag
import com.teksiak.nutrilight.core.presentation.designsystem.GermanyFlag
import com.teksiak.nutrilight.core.presentation.designsystem.ItalyFlag
import com.teksiak.nutrilight.core.presentation.designsystem.PolandFlag
import com.teksiak.nutrilight.core.presentation.designsystem.SpainFlag

data class CountryUi(
    val name: String,
    val code: String,
    val flag: ImageVector
)

@Composable
fun Country.toCountryUi(): CountryUi {
    val (nameRes, flag) = when (this) {
        Country.FRANCE -> R.string.france to FranceFlag
        Country.GERMANY -> R.string.germany to GermanyFlag
        Country.ITALY -> R.string.italy to ItalyFlag
        Country.POLAND -> R.string.poland to PolandFlag
        Country.SPAIN -> R.string.spain to SpainFlag
        Country.UNITED_KINGDOM -> R.string.united_kingdom to FranceFlag
    }
    return CountryUi(
        name = stringResource(nameRes),
        code = this.code,
        flag = flag
    )
}