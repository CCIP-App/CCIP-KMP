/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.eventpreview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.helpers.PortalHelper
import app.opass.ccip.network.models.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EventPreviewViewModel @Inject constructor(
    private val portalHelper: PortalHelper
) : ViewModel() {

    private val TAG = EventPreviewViewModel::class.java.simpleName

    private val _events: MutableStateFlow<List<Event>?> = MutableStateFlow(emptyList())
    val events = _events.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        getEvents()
    }

    fun getEvents(forceReload: Boolean = false) {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                _events.value = portalHelper.getEvents(forceReload)
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch events", exception)
                _events.value = null
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}
