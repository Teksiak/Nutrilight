package com.teksiak.nutrilight.scanner.presentation.analyzer

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.MutableState
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BarcodeImageAnalyzer(
    val onSuccess: (String) -> Unit,
    val onFailure: () -> Unit
) : ImageAnalysis.Analyzer {
    private val scannerOptions = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E,
            Barcode.FORMAT_EAN_8
        )
        .build()

    private val scanner by lazy { BarcodeScanning.getClient(scannerOptions) }
    private var currentTimestamp = 0L
    private var canProcessImage = true

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        currentTimestamp = System.currentTimeMillis()
        if(!canProcessImage) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        mediaImage?.let { image ->
            val inputImage = InputImage.fromMediaImage(
                image,
                imageProxy.imageInfo.rotationDegrees
            )
            scanner.process(inputImage)
                .addOnSuccessListener { barcode ->
                    barcode?.takeIf { it.isNotEmpty() }
                        ?.first()?.rawValue
                        ?.let {
                            Log.d("BarcodeImageAnalyzer", "Barcode detected: $it")
                            onSuccess(it)
                        }
                }
                .addOnFailureListener {
                    onFailure()
                }
                .addOnCompleteListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(500 - (System.currentTimeMillis() - currentTimestamp))
                        imageProxy.close()
                    }
                }
        }
    }

    fun canProcessImage(status: Boolean) {
        canProcessImage = status
    }
}