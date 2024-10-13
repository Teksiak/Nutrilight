package com.teksiak.nutrilight.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teksiak.nutrilight.core.domain.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MoreState())
    val state = _state.asStateFlow()

    init {
        combine(
            settingsRepository.country,
            settingsRepository.showProductImages,
            settingsRepository.historySize,
        ) { country, showProductImages, historySize ->
            MoreState(
                selectedCountry = country,
                showProductImages = showProductImages,
                historySize = historySize
            )
        }.onEach { settings ->
            _state.update {
                it.copy(
                    selectedCountry = settings.selectedCountry,
                    showProductImages = settings.showProductImages,
                    historySize = settings.historySize,
                    areSettingsLoaded = true
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: MoreAction) {
        when (action) {
            is MoreAction.ToggleCountryExplanation -> {
                _state.update { it.copy(showCountryExplanation = !it.showCountryExplanation) }
            }

            is MoreAction.ShowCountrySelect -> {
                _state.update { it.copy(showCountrySelectDialog = true) }
            }

            is MoreAction.SelectCountry -> {
                viewModelScope.launch {
                    settingsRepository.setCountry(action.code)
                }
            }

            is MoreAction.HideCountrySelect -> {
                _state.update { it.copy(showCountrySelectDialog = false) }
            }

            is MoreAction.ShowHistorySizeDialog -> {
                _state.update { it.copy(showHistorySizeDialog = true) }
            }

            is MoreAction.SetHistorySize -> {
                viewModelScope.launch {
                    settingsRepository.setHistorySize(action.size)
                }
            }

            is MoreAction.HideHistorySizeDialog -> {
                _state.update { it.copy(showHistorySizeDialog = false) }
            }

            is MoreAction.ToggleProductImages -> {
                viewModelScope.launch {
                    settingsRepository.toggleShowProductImages()
                }
            }

            else -> {}
        }
    }

}