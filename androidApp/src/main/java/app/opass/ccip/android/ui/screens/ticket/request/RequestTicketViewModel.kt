/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.ticket.request

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.android.ui.extensions.saveToken
import app.opass.ccip.android.ui.extensions.sharedPreferences
import app.opass.ccip.helpers.PortalHelper
import app.opass.ccip.network.models.eventconfig.EventConfig
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import zxingcpp.BarcodeReader

@HiltViewModel(assistedFactory = RequestTicketViewModel.Factory::class)
class RequestTicketViewModel @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted private val eventId: String,
    private val portalHelper: PortalHelper,
    private val barcodeReader: BarcodeReader
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(eventId: String): RequestTicketViewModel
    }

    private val TAG = RequestTicketViewModel::class.java.simpleName

    private val _eventConfig: MutableStateFlow<EventConfig?> = MutableStateFlow(null)
    val eventConfig = _eventConfig.asStateFlow()

    private val _token: MutableSharedFlow<String?> = MutableSharedFlow()
    val token = _token.asSharedFlow()

    private val _isVerifying = MutableStateFlow(false)
    val isVerifying = _isVerifying.asStateFlow()

    init {
        getEventConfig()
    }

    private fun getEventConfig() {
        viewModelScope.launch {
            try {
                _eventConfig.value = portalHelper.getEventConfig(eventId)
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch event config", exception)
                _eventConfig.value = null
            }
        }
    }

    fun getAttendee(token: String) {
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

    fun getAttendee(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bitmap = BitmapFactory.decodeStream(
                    context.contentResolver.openInputStream(uri)
                )
                getAttendee(barcodeReader.read(bitmap).firstOrNull()!!.text!!)
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to find ticket in given image", exception)
                _token.emit(null)
            }
        }
    }
}
