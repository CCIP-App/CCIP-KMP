/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.ticket.show

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.android.extensions.removeToken
import app.opass.ccip.android.extensions.sharedPreferences
import app.opass.ccip.helpers.PortalHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ShowTicketViewModel.Factory::class)
class ShowTicketViewModel @AssistedInject constructor(
    @Assisted("eventId") private val eventId: String,
    @Assisted("token") private val token: String,
    @ApplicationContext private val context: Context,
    private val portalHelper: PortalHelper
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("eventId") eventId: String,
            @Assisted("token") token: String
        ): ShowTicketViewModel
    }

    fun logout() {
        viewModelScope.launch {
            portalHelper.deleteAttendee(eventId, token)
            context.sharedPreferences.removeToken(eventId)
        }
    }
}
