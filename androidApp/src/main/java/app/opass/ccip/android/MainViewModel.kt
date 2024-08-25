/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.network.PortalClient
import app.opass.ccip.network.models.event.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val TAG = MainViewModel::class.java.simpleName

    private val _events: MutableStateFlow<List<Event>?> = MutableStateFlow(emptyList())
    val events = _events.asStateFlow()

    init {
        getEvents()
    }

    fun getEvents() {
        viewModelScope.launch {
            try {
                _events.value = PortalClient.getEvents()
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch events", exception)
                _events.value = null
            }
        }
    }
}
