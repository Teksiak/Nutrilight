package com.teksiak.nutrilight

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class NutrilightApp: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

}