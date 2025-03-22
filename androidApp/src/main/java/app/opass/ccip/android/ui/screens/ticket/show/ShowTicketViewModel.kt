/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.ticket.show

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.android.ui.extensions.removeToken
import app.opass.ccip.android.ui.extensions.sharedPreferences
import app.opass.ccip.helpers.PortalHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowTicketViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val portalHelper: PortalHelper
) : ViewModel() {

    fun logout(eventId: String, token: String) {
        viewModelScope.launch {
            portalHelper.deleteAttendee(eventId, token)
            context.sharedPreferences.removeToken(eventId)
        }
    }
}
