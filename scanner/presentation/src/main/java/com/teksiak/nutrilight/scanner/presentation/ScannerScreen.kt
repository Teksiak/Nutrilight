package com.teksiak.nutrilight.scanner.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.core.presentation.designsystem.BackIcon
import com.teksiak.nutrilight.core.presentation.designsystem.CameraIcon
import com.teksiak.nutrilight.core.presentation.designsystem.FlashFilledIcon
import com.teksiak.nutrilight.core.presentation.designsystem.FlashIcon
import com.teksiak.nutrilight.core.presentation.designsystem.LogoRottenIcon
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.White
import com.teksiak.nutrilight.core.presentation.designsystem.components.LoadingDialog
import com.teksiak.nutrilight.core.presentation.designsystem.components.InteractionDialog
import com.teksiak.nutrilight.core.presentation.designsystem.components.PrimaryButton
import com.teksiak.nutrilight.core.presentation.designsystem.components.SecondaryButton
import com.teksiak.nutrilight.core.presentation.util.ObserveAsEvents
import com.teksiak.nutrilight.core.presentation.util.hasPermission
import com.teksiak.nutrilight.scanner.presentation.analyzer.BarcodeImageAnalyzer
import com.teksiak.nutrilight.scanner.presentation.components.CameraPreview
import com.teksiak.nutrilight.scanner.presentation.components.ScannerOverlay

@Composable
fun ScannerScreenRoot(
    viewModel: ScannerViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToProduct: (String) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(
        flow = viewModel.events
    ) { event ->
        when(event) {
            is ScannerEvent.ProductFound -> {
                onNavigateToProduct(event.productId)
            }
        }
    }

    ScannerScreen(
        state = state,
        onAction = { action ->
            when(action) {
                ScannerAction.NavigateBack -> onNavigateBack()
                ScannerAction.NavigateToSearch -> onNavigateToSearch()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun ScannerScreen(
    state: ScannerState,
    onAction: (ScannerAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            Log.d("ScannerScreen", "Launcher result")
            val activity = context as ComponentActivity
            val showCameraRationale = activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)

            onAction(
                ScannerAction.RequestCameraPermission(
                    acceptedCameraPermission = isGranted,
                    showCameraPermissionRationale = showCameraRationale
                )
            )
        }
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsStateWithLifecycle()
    
    LaunchedEffect(lifecycleState) {
        if(lifecycleState == Lifecycle.State.RESUMED) {
            val activity = context as ComponentActivity
            val hasCameraPermission = context.hasPermission(Manifest.permission.CAMERA)
            val showCameraRationale = activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)

            onAction(
                ScannerAction.SubmitCameraPermissionInfo(
                    acceptedCameraPermission = hasCameraPermission,
                    showCameraPermissionRationale = showCameraRationale
                )
            )

            if(!hasCameraPermission && !state.requestedCameraPermission) {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    if(!state.acceptedCameraPermission && state.requestedCameraPermission) {
        if(state.showCameraPermissionRationale) {
            InteractionDialog(
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
                    SecondaryButton(
                        text = stringResource(id = R.string.allow),
                        onClick = {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                        color = Primary
                    )
                },
                onBackPressed = {
                    onAction(ScannerAction.NavigateBack)
                }
            )
        } else {
            InteractionDialog(
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
                    SecondaryButton(
                        text = stringResource(id = R.string.go_to_settings),
                        onClick = {
                            (context as Activity).openAppSettings()
                        },
                        color = Primary
                    )
                },
                onBackPressed = {
                    onAction(ScannerAction.NavigateBack)
                }
            )
        }
    }

    if(state.acceptedCameraPermission) {
        val imageAnalyzer = remember {
            BarcodeImageAnalyzer(
                onSuccess = { barcode ->
                    onAction(ScannerAction.BarcodeDetected(barcode))
                },
                onFailure = {
                    onAction(ScannerAction.ScannerError)
                }
            )
        }
        val cameraController = remember {
            LifecycleCameraController(context).apply {
                setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
                imageAnalysisBackpressureStrategy = ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                setImageAnalysisAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    imageAnalyzer
                )
            }
        }

        LaunchedEffect(lifecycleState) {
            if(lifecycleState == Lifecycle.State.RESUMED && state.isFlashOn) {
                cameraController.enableTorch(true)
            }
        }

        LaunchedEffect(state.shouldProcessImage) {
            imageAnalyzer.canProcessImage(state.shouldProcessImage)
        }

        if(state.scannerError) {
            InteractionDialog(
                title = stringResource(id = R.string.whoops),
                description = stringResource(id = R.string.something_went_wrong),
                icon = {
                    Icon(
                        modifier = Modifier.size(64.dp),
                        imageVector = LogoRottenIcon,
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                },
                buttons = {
                    SecondaryButton(
                        text = stringResource(id = R.string.try_again),
                        onClick = {
                            onAction(ScannerAction.DismissError)
                        },
                    )
                },
                onDismiss = {
                    onAction(ScannerAction.DismissError)
                }
            )
        } else if(state.productNotFound) {
            InteractionDialog(
                title = stringResource(id = R.string.hmm),
                description = stringResource(id = R.string.product_not_found),
                icon = {
                    Icon(
                        modifier = Modifier.size(64.dp),
                        imageVector = LogoRottenIcon,
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                },
                buttons = {
                    PrimaryButton(
                        text = stringResource(id = R.string.search),
                        onClick = {
                            onAction(ScannerAction.NavigateToSearch)
                        },
                    )
                },
                bottomText = "Scanned ID: ${state.scannedId ?: "-"}",
                onDismiss = {
                    onAction(ScannerAction.DismissError)
                }
            )
        } else if(state.isLoading) {
            LoadingDialog(
                modifier = Modifier
                    .padding(48.dp)
                    .width(230.dp)
                    .height(152.dp)
                ,
                text = stringResource(id = R.string.checking_the_ingredients),
                onBackPressed = {
                    onAction(ScannerAction.NavigateBack)
                }
            )
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                CameraPreview(
                    cameraController = cameraController
                )

                ScannerOverlay(
                    paddingValues = paddingValues
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            onAction(ScannerAction.NavigateBack)
                        }
                    ) {
                        Icon(
                            imageVector = BackIcon,
                            contentDescription = "Back",
                            tint = White
                        )
                    }
                    IconButton(
                        onClick = {
                            cameraController.enableTorch(!state.isFlashOn)
                            onAction(ScannerAction.ToggleFlash)
                        }
                    ) {
                        Icon(
                            imageVector = if(state.isFlashOn) FlashFilledIcon else FlashIcon,
                            contentDescription = if(state.isFlashOn) "Flash off" else "Flash on",
                            tint = White
                        )
                    }
                }
            }
        }
    }
}

private fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}