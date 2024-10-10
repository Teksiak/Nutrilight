package com.teksiak.nutrilight.more

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.R
import com.teksiak.nutrilight.core.domain.SettingsRepository
import com.teksiak.nutrilight.core.presentation.BottomNavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.HelpCircleIcon
import com.teksiak.nutrilight.core.presentation.designsystem.LogoIcon
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightSwitch
import com.teksiak.nutrilight.core.presentation.util.bottomBorder
import com.teksiak.nutrilight.more.components.CountryDialog
import com.teksiak.nutrilight.more.components.CountrySelect
import com.teksiak.nutrilight.more.components.HistorySizeDialog
import com.teksiak.nutrilight.more.util.toCountryUi

@Composable
fun MoreScreenRoot(
    viewModel: MoreViewModel,
    onNavigateBack: () -> Unit,
    navigateWithTab: (BottomNavigationTab) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler {
        onNavigateBack()
    }

    HomeScreen(
        state = state,
        onAction = { action ->
            when (action) {
                else -> Unit
            }
            viewModel.onAction(action)
        },
        navigateWithTab = navigateWithTab
    )
}

@Composable
private fun HomeScreen(
    state: MoreState,
    onAction: (MoreAction) -> Unit,
    navigateWithTab: (BottomNavigationTab) -> Unit
) {
    if (state.showCountrySelectDialog) {
        CountryDialog(
            onCountrySelect = { country ->
                onAction(MoreAction.SelectCountry(country))
            },
            onDismiss = {
                onAction(MoreAction.HideCountrySelect)
            }
        )
    }

    if(state.showHistorySizeDialog) {
        HistorySizeDialog(
            selectedSizeIndex = SettingsRepository.HISTORY_SIZES.indexOf(state.historySize),
            onHistorySizeSelect = { sizeIndex ->
                onAction(MoreAction.SetHistorySize(SettingsRepository.HISTORY_SIZES[sizeIndex]))
            },
            onDismiss = {
                onAction(MoreAction.HideHistorySizeDialog)
            }
        )
    }

    NutrilightScaffold(
        topAppBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomBorder(
                        strokeWidth = 1.dp,
                        color = ShadedWhite
                    )
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.nutrilight),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = stringResource(R.string.shining_light),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Image(
                    imageVector = LogoIcon,
                    contentDescription = stringResource(R.string.logo),
                )
            }
        },
        currentTab = BottomNavigationTab.More,
        onTabSelected = navigateWithTab
    ) { innerPadding ->
        if (!state.areSettingsLoaded) return@NutrilightScaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            AppSettings(
                state = state,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun AppSettings(
    state: MoreState,
    onAction: (MoreAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.app_settings),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.your_country),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                onAction(MoreAction.ToggleCountryExplanation)
                            }
                        )
                    },
                imageVector = HelpCircleIcon,
                contentDescription = stringResource(R.string.show_explaination),
                tint = Silver
            )
            CountrySelect(
                selectedCountry = state.selectedCountry.toCountryUi(),
                onClick = {
                    onAction(MoreAction.ShowCountrySelect)
                }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        AnimatedVisibility(
            visible = state.showCountryExplanation,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                text = stringResource(R.string.country_explanation),
                style = MaterialTheme.typography.bodyMedium,
                color = Silver
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.show_product_images),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            NutrilightSwitch(
                checked = state.showProductImages,
                onCheckedChange = {
                    onAction(MoreAction.ToggleProductImages)
                }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.history_size),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onAction(MoreAction.ShowHistorySizeDialog) }
                    .border(
                        width = 1.dp,
                        color = ShadedWhite,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.historySize.toProducts(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Primary
                )
            }
        }
    }
}

private fun Int.toProducts() = "$this products"

@Preview
@Composable
private fun HomeScreenPreview() {
    NutrilightTheme {
        HomeScreen(
            state = MoreState(
                showCountryExplanation = true,
                areSettingsLoaded = true
            ),
            onAction = {},
            navigateWithTab = {}
        )
    }
}