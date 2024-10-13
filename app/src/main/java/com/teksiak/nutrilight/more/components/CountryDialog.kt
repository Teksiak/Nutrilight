package com.teksiak.nutrilight.more.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.teksiak.nutrilight.R
import com.teksiak.nutrilight.core.domain.Country
import com.teksiak.nutrilight.core.presentation.designsystem.CheckIcon
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.White
import com.teksiak.nutrilight.core.presentation.designsystem.XCloseIcon
import com.teksiak.nutrilight.more.util.CountryUi
import com.teksiak.nutrilight.more.util.toCountryUi

@Composable
fun CountryDialog(
    selectedCountry: Country,
    suggestedCountries: List<Country>,
    onCountrySelect: (String) -> Unit,
    onDismiss: () -> Unit = {}
) {
    val isSuggestedSelected = remember { suggestedCountries.contains(selectedCountry) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(White)
                .padding(24.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = 12.dp, y = (-12).dp),
                onClick = onDismiss
            ) {
                Icon(
                    imageVector = XCloseIcon,
                    contentDescription = stringResource(R.string.close_dialog),
                    tint = Silver
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.select_country),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    text = stringResource(R.string.suggested),
                    style = MaterialTheme.typography.bodySmall,
                    color = if(isSuggestedSelected) Primary else ShadedWhite,
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = if(isSuggestedSelected) Primary else ShadedWhite,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp)
                ) {
                    suggestedCountries.map { it.toCountryUi() }.forEach { countryUi ->
                        CountryItem(
                            modifier = Modifier.fillMaxWidth(),
                            countryUi = countryUi,
                            isSelected = countryUi.code == selectedCountry.code,
                            onClick = {
                                onCountrySelect(countryUi.code)
                                onDismiss()
                            }
                        )
                        if (countryUi.code != Country.entries.last().code) {
                            HorizontalDivider(
                                color = if(isSuggestedSelected) Primary else ShadedWhite,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))

                (Country.entries - suggestedCountries.toSet()).map { it.toCountryUi() }.forEach { countryUi ->
                    CountryItem(
                        modifier = Modifier.fillMaxWidth(),
                        countryUi = countryUi,
                        isSelected = countryUi.code == selectedCountry.code,
                        onClick = {
                            onCountrySelect(countryUi.code)
                            onDismiss()
                        }
                    )
                    if (countryUi.code != Country.entries.last().code) {
                        HorizontalDivider(
                            color = ShadedWhite,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CountryItem(
    countryUi: CountryUi,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Row(
        modifier = modifier
            .clickable {
                onClick()
            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            imageVector = countryUi.flag,
            contentDescription = countryUi.name,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
        )
        Text(
            text = countryUi.name,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isSelected) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = CheckIcon,
                contentDescription = stringResource(R.string.selected),
                tint = Primary
            )
        }
    }
}

@Preview
@Composable
private fun CountryDialogPreview() {
    NutrilightTheme {
        CountryDialog(
            selectedCountry = Country.UNITED_KINGDOM,
            suggestedCountries = listOf(Country.UNITED_KINGDOM, Country.POLAND),
            onCountrySelect = {}
        )
    }
}
