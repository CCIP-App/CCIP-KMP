/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.announcement

import android.util.Log
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

    private val TAG = AnnouncementViewModel::class.java.simpleName

    private val _announcements: MutableStateFlow<List<Announcement>?> = MutableStateFlow(emptyList())
    val announcements = _announcements.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun getAnnouncements(eventId: String, token: String? = null, forceReload: Boolean = false) {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                _announcements.value = portalHelper.getAnnouncements(eventId, token, forceReload)
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch announcements", exception)
                _announcements.value = null
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}
