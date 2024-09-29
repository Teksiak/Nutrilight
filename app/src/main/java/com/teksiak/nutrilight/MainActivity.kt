package com.teksiak.nutrilight

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.scanner.presentation.ScannerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val activity = LocalView.current.context as Activity

            WindowCompat.getInsetsController(activity.window, activity.window.decorView).apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true
            }

            val navController = rememberNavController()

            NutrilightTheme {
                Surface {
                    NavigationRoot(
                        navController = navController,
                        closeApp = { finish() }
                    )
                }
            }
        }
    }
}