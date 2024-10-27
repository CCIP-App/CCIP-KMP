/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.announcement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.helpers.PortalHelper
import app.opass.ccip.network.models.fastpass.Announcement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val portalHelper: PortalHelper
): ViewModel() {

    private val _announcements: MutableStateFlow<List<Announcement>> = MutableStateFlow(emptyList())
    val announcements = _announcements.asStateFlow()

    fun getAnnouncements(eventId: String, token: String? = null, forceReload: Boolean = false) {
        viewModelScope.launch {
            _announcements.value = portalHelper.getAnnouncements(eventId, token, forceReload)
        }
    }
}
