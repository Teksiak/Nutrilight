package com.teksiak.nutrilight.home

import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {

        fun onAction(action: HomeAction) {
            when (action) {
                is HomeAction.ScanBarcode -> { }
            }
        }
}