/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.ticket

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.android.ui.extensions.removeToken
import app.opass.ccip.android.ui.extensions.saveToken
import app.opass.ccip.android.ui.extensions.sharedPreferences
import app.opass.ccip.helpers.PortalHelper
import app.opass.ccip.network.models.eventconfig.EventConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zxingcpp.BarcodeReader
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val portalHelper: PortalHelper,
    private val barcodeReader: BarcodeReader
): ViewModel() {

    private val TAG = TicketViewModel::class.java.simpleName

    private val _eventConfig: MutableStateFlow<EventConfig?> = MutableStateFlow(null)
    val eventConfig = _eventConfig.asStateFlow()

    private val _token: MutableStateFlow<String?> = MutableStateFlow(null)
    val token = _token.asStateFlow()

    private val _isVerifying = MutableStateFlow(false)
    val isVerifying = _isVerifying.asStateFlow()

    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest = _surfaceRequest.asStateFlow()

    private val cameraPreview = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
        }
    }

    private val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build().apply {
            setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                barcodeReader.read(imageProxy).firstOrNull()?.let { result ->
                    if (result.format == BarcodeReader.Format.QR_CODE) {
                        if (!result.text.isNullOrBlank()) {
                            getAttendee(_eventConfig.value!!.id, result.text!!)
                        }
                    }
                }
                imageProxy.close()
            }
        }

    fun getEventConfig(eventId: String) {
        viewModelScope.launch {
            try {
                _eventConfig.value = portalHelper.getEventConfig(eventId)
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch event config", exception)
                _eventConfig.value = null
            }
        }
    }

    fun getAttendee(eventId: String, token: String) {
        viewModelScope.launch {
            try {
                _isVerifying.value = true
                if (portalHelper.getAttendee(eventId, token, true) != null) {
                    Log.i(TAG, "Token is valid")
                    context.sharedPreferences.saveToken(eventId, token)
                    _token.emit(token)
                }
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch attendee", exception)
                _token.emit(null)
            } finally {
                _isVerifying.value = false
            }
        }
    }

    fun logout(eventId: String, token: String) {
        viewModelScope.launch {
            portalHelper.deleteAttendee(eventId, token)
            context.sharedPreferences.removeToken(eventId)
            _token.emit(null)
        }
    }

    fun bindToCamera(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            val processCameraProvider = ProcessCameraProvider.awaitInstance(context)
            processCameraProvider.bindToLifecycle(
                lifecycleOwner, DEFAULT_BACK_CAMERA, imageAnalysis, cameraPreview
            )

            try { awaitCancellation() } finally { processCameraProvider.unbindAll() }
        }
    }
}
