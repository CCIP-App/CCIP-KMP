/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.helpers.PortalHelper
import app.opass.ccip.network.models.schedule.Schedule
import app.opass.ccip.network.models.schedule.Session
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@HiltViewModel(assistedFactory = ScheduleViewModel.Factory::class)
class ScheduleViewModel @AssistedInject constructor(
    @Assisted private val eventId: String,
    val sdf: SimpleDateFormat,
    private val portalHelper: PortalHelper
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(eventId: String): ScheduleViewModel
    }

    private val TAG = ScheduleViewModel::class.java.simpleName

    private val _schedule: MutableStateFlow<Schedule?> = MutableStateFlow(null)
    val schedule = _schedule.asStateFlow()

    private val _searchResult: MutableStateFlow<List<Session>> = MutableStateFlow(emptyList())
    val searchResult = _searchResult.asStateFlow()

    init {
        getSchedule()
    }

    private fun getSchedule(forceReload: Boolean = false) {
        viewModelScope.launch {
            try {
                val rawSchedule = portalHelper.getSchedule(eventId, forceReload)
                _schedule.value = rawSchedule?.copy(
                    sessions = rawSchedule.sessions.map {
                        it.copy(room = portalHelper.getRoom(eventId, it.room)!!.name)
                    }
                )
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
}
