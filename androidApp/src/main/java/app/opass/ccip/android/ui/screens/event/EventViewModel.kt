/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.event

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.helpers.PortalHelper
import app.opass.ccip.network.models.eventconfig.EventConfig
import app.opass.ccip.network.models.schedule.Schedule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@HiltViewModel
class EventViewModel @Inject constructor(
    val sdf: SimpleDateFormat,
    private val portalHelper: PortalHelper
): ViewModel() {

    private val TAG = EventViewModel::class.java.simpleName

    private val _eventConfig: MutableStateFlow<EventConfig?> = MutableStateFlow(null)
    val eventConfig = _eventConfig.asStateFlow()

    private val _schedule: MutableStateFlow<Schedule?> = MutableStateFlow(null)
    val schedule = _schedule.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun getEventConfig(eventId: String, forceReload: Boolean = false) {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                _eventConfig.value = portalHelper.getEventConfig(eventId, forceReload)
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch event config", exception)
                _eventConfig.value = null
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun getSchedule(eventId: String, forceReload: Boolean = false) {
        viewModelScope.launch {
            try {
                _schedule.value = portalHelper.getSchedule(eventId, forceReload)
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch schedules", exception)
                _schedule.value = null
            }
        }
    }
}
