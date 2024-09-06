package com.teksiak.nutrilight.scanner.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.core.presentation.designsystem.CameraIcon
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.TintedBlack
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightDialog
import com.teksiak.nutrilight.core.presentation.designsystem.components.OutlinedButton
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
            NutrilightDialog(
                title = stringResource(id = R.string.camera_permission_rationale_title),
                description = stringResource(id = R.string.camera_permission_rationale_description),
                isDismissible = false,
                icon = {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        imageVector = CameraIcon,
                        contentDescription = "Camera icon",
                        tint = Primary
                    )
                },
                buttons = {
                    OutlinedButton(
                        text = stringResource(id = R.string.allow),
                        onClick = {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                        color = Primary
                    )
                }
            )
        } else {
            NutrilightDialog(
                title = stringResource(id = R.string.camera_permission_rationale_title),
                description = stringResource(id = R.string.camera_permission_denied_description),
                isDismissible = false,
                icon = {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        imageVector = CameraIcon,
                        contentDescription = "Camera icon",
                        tint = Primary
                    )
                },
                buttons = {
                    OutlinedButton(
                        text = stringResource(id = R.string.go_to_settings),
                        onClick = {
                            (context as Activity).openAppSettings()
                        },
                        color = Primary
                    )
                }
            )
        }
    }
}

private fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}