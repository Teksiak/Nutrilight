package com.teksiak.nutrilight.more

sealed interface MoreEvent {
    data object ShowHistorySizeDialog: MoreEvent
}