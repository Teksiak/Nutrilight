package com.teksiak.nutrilight.scanner.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.core.presentation.util.hasPermission


@Composable
fun ScannerScreenRoot(
    onNavigateBack: () -> Unit,
    viewModel: ScannerViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScannerScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ScannerScreen(
    state: ScannerState,
    onAction: (ScannerAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            val activity = context as ComponentActivity
            val showCameraRationale = activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)

            onAction(
                ScannerAction.SubmitCameraPermissionInfo(
                    acceptedCameraPermission = isGranted,
                    showCameraPermissionRationale = showCameraRationale
                )
            )
        }
    )

    LaunchedEffect(Unit) {
        val activity = context as ComponentActivity
        val hasCameraPermission = context.hasPermission(Manifest.permission.CAMERA)
        val showCameraRationale = activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)

        onAction(
            ScannerAction.SubmitCameraPermissionInfo(
                acceptedCameraPermission = hasCameraPermission,
                showCameraPermissionRationale = showCameraRationale
            )
        )

        if(!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if(!state.acceptedCameraPermission) {
        if(state.showCameraPermissionRationale) {
            // Show dialog to request permission
        } else {
            // Show dialog to go to settings
        }
    }
}

private fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
    )
}