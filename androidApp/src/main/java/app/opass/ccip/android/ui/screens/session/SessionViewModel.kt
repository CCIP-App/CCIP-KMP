/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.helpers.PortalHelper
import app.opass.ccip.network.models.schedule.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    val sdf: SimpleDateFormat,
    private val portalHelper: PortalHelper
): ViewModel() {

    private val _session: MutableStateFlow<Session?> = MutableStateFlow(null)
    val session = _session.asStateFlow()

    fun getSession(eventId: String, sessionId: String) {
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
                speakers = queriedSpeakers
            )
        }
    }
}
