/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.event

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.android.extensions.getToken
import app.opass.ccip.android.extensions.sharedPreferences
import app.opass.ccip.helpers.PortalHelper
import app.opass.ccip.network.models.eventconfig.EventConfig
import app.opass.ccip.network.models.fastpass.Attendee
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EventViewModel.Factory::class)
class EventViewModel @AssistedInject constructor(
    @Assisted private val eventId: String,
    private val portalHelper: PortalHelper,
    @ApplicationContext private val context: Context
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(eventId: String): EventViewModel
    }

    private val TAG = EventViewModel::class.java.simpleName

    private val _eventConfig: MutableStateFlow<EventConfig?> = MutableStateFlow(null)
    val eventConfig = _eventConfig.asStateFlow()

    private val _attendee: MutableStateFlow<Attendee?> = MutableStateFlow(null)
    val attendee = _attendee.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        getEventConfig()
    }

    private fun getEventConfig(forceReload: Boolean = false) {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                _eventConfig.value = portalHelper.getEventConfig(eventId, forceReload)

                // Fetch attendee as well
                getAttendee(forceReload)
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch event config", exception)
                _eventConfig.value = null
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun getAttendee(forceReload: Boolean = false) {
        viewModelScope.launch {
            try {
                val token = context.sharedPreferences.getToken(eventId)
                if (token != null) {
                    _attendee.value = portalHelper.getAttendee(eventId, token, forceReload)
                } else {
                    _attendee.value = null
                }
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch attendee", exception)
                _attendee.value = null
            }
        }
    }
}
