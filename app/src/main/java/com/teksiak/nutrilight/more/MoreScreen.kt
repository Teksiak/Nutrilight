package com.teksiak.nutrilight.more

import android.content.res.Resources
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.R
import com.teksiak.nutrilight.core.domain.Country
import com.teksiak.nutrilight.core.domain.SettingsRepository
import com.teksiak.nutrilight.core.presentation.NavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.HelpCircleIcon
import com.teksiak.nutrilight.core.presentation.designsystem.LogoIcon
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightSwitch
import com.teksiak.nutrilight.core.presentation.util.bottomBorder
import com.teksiak.nutrilight.more.components.CountrySelectDialog
import com.teksiak.nutrilight.more.components.CountrySelect
import com.teksiak.nutrilight.more.components.HistorySizeDialog
import com.teksiak.nutrilight.core.presentation.ui_models.toCountryUi
import com.teksiak.nutrilight.core.presentation.util.ObserveAsEvents
import com.teksiak.nutrilight.more.util.simulatePress
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun MoreScreenRoot(
    viewModel: MoreViewModel,
    onNavigateBack: () -> Unit,
    navigateWithTab: (NavigationTab) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler {
        onNavigateBack()
    }

    MoreScreen(
        state = state,
        events = viewModel.events,
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
private fun MoreScreen(
    state: MoreState,
    events: Flow<MoreEvent>,
    onAction: (MoreAction) -> Unit,
    navigateWithTab: (NavigationTab) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val suggestedCountries = remember {
        val locales = Resources.getSystem().configuration.locales
        (0 until locales.size()).mapNotNull { index ->
            Country.fromLocale(locales[index])
        }
    }
    val historySizeInteractionSource = remember { MutableInteractionSource() }

    ObserveAsEvents(flow = events) { event ->
        when(event) {
            is MoreEvent.ShowHistorySizeDialog -> {
                coroutineScope.launch {
                    simulatePress(historySizeInteractionSource)
                }
            }
        }
    }

    if (state.showCountrySelectDialog) {
        CountrySelectDialog(
            selectedCountry = state.selectedCountry,
            suggestedCountries = suggestedCountries,
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
        currentTab = NavigationTab.More,
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
                onAction = onAction,
                historySizeInteractionSource = historySizeInteractionSource
            )
        }
    }
}

@Composable
private fun AppSettings(
    state: MoreState,
    onAction: (MoreAction) -> Unit,
    modifier: Modifier = Modifier,
    historySizeInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() }
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
                },
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
            HistorySizeButton(
                size = state.historySize,
                onClick = {
                    onAction(MoreAction.ShowHistorySizeDialog)
                },
                interactionSource = historySizeInteractionSource
            )
        }
    }
}

@Composable
private fun HistorySizeButton(
    size: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick
            )
            .border(
                width = 1.dp,
                color = ShadedWhite,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$size products",
            style = MaterialTheme.typography.bodyLarge,
            color = Primary
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    NutrilightTheme {
        MoreScreen(
            state = MoreState(
                showCountryExplanation = true,
                areSettingsLoaded = true
            ),
            events = emptyFlow(),
            onAction = {},
            navigateWithTab = {}
        )
    }
}