package com.teksiak.nutrilight.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teksiak.nutrilight.core.domain.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        settingsRepository.countryCode
            .onEach { country ->
                _state.update {
                    it.copy(selectedCountry = country, areSettingsLoaded = true)
                }
            }
            .launchIn(viewModelScope)

        settingsRepository.showProductImages
            .onEach { showProductImages ->
                _state.update {
                    it.copy(showProductImages = showProductImages, areSettingsLoaded = true)
                }
            }
            .launchIn(viewModelScope)
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
                // TODO: Implement error ui handling
                viewModelScope.launch {
                    settingsRepository.setCountryCode(action.code)
                }
            }

            is MoreAction.HideCountrySelect -> {
                _state.update { it.copy(showCountrySelectDialog = false) }
            }

            is MoreAction.ToggleProductImages -> {
                // TODO: Implement error ui handling
                viewModelScope.launch {
                    settingsRepository.toggleShowProductImages()
                }
            }

            else -> {}
        }
    }

}