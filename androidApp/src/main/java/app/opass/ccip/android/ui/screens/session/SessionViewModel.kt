/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.helpers.PortalHelper
import app.opass.ccip.network.models.schedule.Session
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@HiltViewModel(assistedFactory = SessionViewModel.Factory::class)
class SessionViewModel @AssistedInject constructor(
    val sdf: SimpleDateFormat,
    @Assisted("eventId") private val eventId: String,
    @Assisted("sessionId") private val sessionId: String,
    private val portalHelper: PortalHelper
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("eventId") eventId: String,
            @Assisted("sessionId") sessionId: String
        ): SessionViewModel
    }

    private val _session: MutableStateFlow<Session?> = MutableStateFlow(null)
    val session = _session.asStateFlow()

    init {
        getSession()
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
                speakers = queriedSpeakers
            )
        }
    }
}
