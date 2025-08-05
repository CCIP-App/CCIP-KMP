/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.session

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.android.extensions.getAlarms
import app.opass.ccip.android.extensions.getBookmarks
import app.opass.ccip.android.extensions.saveAlarms
import app.opass.ccip.android.extensions.saveBookmarks
import app.opass.ccip.android.extensions.sharedPreferences
import app.opass.ccip.android.utils.AlarmUtil
import app.opass.ccip.helpers.PortalHelper
import app.opass.ccip.network.models.schedule.Session
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@HiltViewModel(assistedFactory = SessionViewModel.Factory::class)
class SessionViewModel @AssistedInject constructor(
    private val sdf: SimpleDateFormat,
    @Assisted("eventId") private val eventId: String,
    @Assisted("sessionId") private val sessionId: String,
    private val portalHelper: PortalHelper,
    @ApplicationContext private val context: Context
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("eventId") eventId: String,
            @Assisted("sessionId") sessionId: String
        ): SessionViewModel
    }

    private val _session: MutableStateFlow<Session?> = MutableStateFlow(null)
    val session = _session.asStateFlow()

    val startTime: Long
        get() = sdf.parse(session.value!!.start)!!.time

    val endTime: Long
        get() = sdf.parse(session.value!!.end)!!.time

    private val _bookmark: MutableStateFlow<Boolean> = MutableStateFlow(
        sessionId in context.sharedPreferences.getBookmarks(eventId)
    )
    val bookmark = _bookmark.asStateFlow()

    private val _alarm: MutableStateFlow<Boolean> = MutableStateFlow(
        sessionId in context.sharedPreferences.getAlarms(eventId)
    )
    val alarm = _alarm.asStateFlow()

    init {
        getSession()
    }

    fun setBookmark(value: Boolean) {
        _bookmark.value = value
        with(context.sharedPreferences) {
            val newSet = getBookmarks(eventId).toMutableSet()
            if (value) newSet.add(sessionId) else newSet.remove(sessionId)
            saveBookmarks(eventId, newSet)
        }
    }

    fun setAlarm(value: Boolean) {
        _alarm.value = value
        with(context.sharedPreferences) {
            val newSet = getAlarms(eventId).toMutableSet()
            if (value) {
                newSet.add(sessionId)
                AlarmUtil.setSessionAlarm(context, startTime, eventId, session.value!!)
            } else {
                newSet.remove(sessionId)
                AlarmUtil.cancelSessionAlarm(context, eventId, sessionId)
            }
            saveAlarms(eventId, newSet)
        }
    }

    private fun getSession() {
        viewModelScope.launch {
            val session = portalHelper.getSession(eventId, sessionId) ?: return@launch

            // Resolve tags, speakers and session type to their original values
            val queriedTags = session.tags?.map { portalHelper.getTag(eventId, it)!!.name }
            val queriedSpeakers = session.speakers.map { portalHelper.getSpeaker(eventId, it)!!.name }
            val queriedSessionType = if (session.type != null) {
                portalHelper.getSessionType(eventId, session.type!!)!!.name
            } else {
                null
            }

            _session.value = session.copy(
                type = queriedSessionType,
                tags = queriedTags,
                speakers = queriedSpeakers,
                room = portalHelper.getRoom(eventId, session.room)!!.name
            )
        }
    }
}
