/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.ticket.scan

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.core.content.getSystemService
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.android.ui.extensions.saveToken
import app.opass.ccip.android.ui.extensions.sharedPreferences
import app.opass.ccip.helpers.PortalHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import zxingcpp.BarcodeReader
import java.util.concurrent.ExecutorService

@HiltViewModel(assistedFactory = ScanTicketViewModel.Factory::class)
class ScanTicketViewModel @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted private val eventId: String,
    private val executorService: ExecutorService,
    private val barcodeReader: BarcodeReader,
    private val portalHelper: PortalHelper
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(eventId: String): ScanTicketViewModel
    }

    private val TAG = ScanTicketViewModel::class.java.simpleName

    private val _token: MutableSharedFlow<String?> = MutableSharedFlow()
    val token = _token.asSharedFlow()

    private val _isVerifying = MutableStateFlow(false)
    val isVerifying = _isVerifying.asStateFlow()

    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest = _surfaceRequest.asStateFlow()

    private lateinit var cameraControl: CameraControl
    private val cameraPreview = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
        }
    }

    private val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build().apply {
            setAnalyzer(executorService) { imageProxy ->
                imageProxy.use { input ->
                    barcodeReader.read(input).firstOrNull()?.text?.let { token ->
                        makeOneShotVibration()
                        runBlocking { getAttendee(eventId, token) }

                        // Avoid scanning the QR multiple times
                        Thread.sleep(2000)
                    }
                }
            }
        }

    fun bindToCamera(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            val processCameraProvider = ProcessCameraProvider.awaitInstance(context)
            processCameraProvider.bindToLifecycle(
                lifecycleOwner, DEFAULT_BACK_CAMERA, imageAnalysis, cameraPreview
            ).also {
                cameraControl = it.cameraControl
            }

            try { awaitCancellation() } finally { processCameraProvider.unbindAll() }
        }
    }

    fun toggleFlash(on: Boolean) {
        cameraControl.enableTorch(on)
    }

    private suspend fun getAttendee(eventId: String, token: String) {
        return try {
            _isVerifying.value = true
            if (portalHelper.getAttendee(eventId, token, true) != null) {
                Log.i(TAG, "Token is valid")
                context.sharedPreferences.saveToken(eventId, token)
                _token.emit(token)
            } else {
                Log.i(TAG, "Token is not valid!")
                _token.emit(token)
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to fetch attendee", exception)
            _token.emit(null)
        } finally {
            _isVerifying.value = false
        }
    }

    private fun makeOneShotVibration() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService<VibratorManager>()?.defaultVibrator
        } else {
            context.getSystemService<Vibrator>()
        } ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(500)
        }
    }
}
