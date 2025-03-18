/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.ticket

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.android.ui.extensions.removeToken
import app.opass.ccip.android.ui.extensions.saveToken
import app.opass.ccip.android.ui.extensions.sharedPreferences
import app.opass.ccip.helpers.PortalHelper
import app.opass.ccip.network.models.eventconfig.EventConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val portalHelper: PortalHelper
): ViewModel() {

    private val TAG = TicketViewModel::class.java.simpleName

    private val _eventConfig: MutableStateFlow<EventConfig?> = MutableStateFlow(null)
    val eventConfig = _eventConfig.asStateFlow()

    private val _token: MutableSharedFlow<String?> = MutableSharedFlow()
    val token = _token.asSharedFlow()

    private val _isVerifying = MutableStateFlow(false)
    val isVerifying = _isVerifying.asStateFlow()

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
}
