package com.teksiak.nutrilight.more

import androidx.lifecycle.ViewModel
import com.teksiak.nutrilight.core.domain.Country
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MoreViewModel : ViewModel() {

    private val _state = MutableStateFlow(MoreState())
    val state = _state.asStateFlow()

    fun onAction(action: MoreAction) {
        when (action) {
            is MoreAction.ShowCountryExplanation -> {
                _state.update { it.copy(showCountryExplanation = true) }
            }
            is MoreAction.ShowCountrySelect -> {
                _state.update { it.copy(showCountrySelectDialog = true) }
            }
            is MoreAction.SelectCountry -> {
                // TODO: Implement Data store with selected country
                _state.update { it.copy(selectedCountry = Country.fromCode(action.code)) }
            }
            is MoreAction.HideCountrySelect -> {
                _state.update { it.copy(showCountrySelectDialog = false) }
            }
            is MoreAction.ToggleProductImages -> {
                // TODO: Implement Data store with show product images
                _state.update { it.copy(showProductImages = !it.showProductImages) }
            }
            else -> {}
        }
    }

}