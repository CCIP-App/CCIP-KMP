/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.event

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.android.ui.extensions.getToken
import app.opass.ccip.android.ui.extensions.sharedPreferences
import app.opass.ccip.helpers.PortalHelper
import app.opass.ccip.network.models.eventconfig.EventConfig
import app.opass.ccip.network.models.fastpass.Attendee
import app.opass.ccip.network.models.schedule.Schedule
import app.opass.ccip.network.models.schedule.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    val sdf: SimpleDateFormat,
    private val portalHelper: PortalHelper,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val TAG = EventViewModel::class.java.simpleName

    private val _eventConfig: MutableStateFlow<EventConfig?> = MutableStateFlow(null)
    val eventConfig = _eventConfig.asStateFlow()

    private val _schedule: MutableStateFlow<Schedule?> = MutableStateFlow(null)
    val schedule = _schedule.asStateFlow()

    private val _attendee: MutableStateFlow<Attendee?> = MutableStateFlow(null)
    val attendee = _attendee.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _searchResult: MutableStateFlow<List<Session>> = MutableStateFlow(emptyList())
    val searchResult = _searchResult.asStateFlow()

    fun getEventConfig(eventId: String, forceReload: Boolean = false) {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                _eventConfig.value = portalHelper.getEventConfig(eventId, forceReload)

                // Fetch attendee as well
                getAttendee(eventId, forceReload)
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
                _searchResult.value = _schedule.value?.sessions ?: emptyList()
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch schedules", exception)
                _schedule.value = null
            }
        }
    }

    fun search(query: String) {
        if (query.isNotBlank()) {
            _searchResult.value = _schedule.value!!.sessions.filter { session ->
                session.title.contains(query, true) || session.speakers.any { it.contains(query, true) }
            }
        } else {
            _searchResult.value = _schedule.value!!.sessions
        }
    }

    private fun getAttendee(eventId: String, forceReload: Boolean = false) {
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
