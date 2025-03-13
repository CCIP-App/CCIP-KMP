/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.preview

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
class PreviewViewModel @Inject constructor(
    private val portalHelper: PortalHelper
) : ViewModel() {

    private val TAG = PreviewViewModel::class.java.simpleName

    private val _events: MutableStateFlow<List<Event>?> = MutableStateFlow(emptyList())
    val events = _events.asStateFlow()

    private val _searchResult: MutableStateFlow<List<Event>> = MutableStateFlow(emptyList())
    val searchResult = _searchResult.asStateFlow()

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
                _searchResult.value = _events.value ?: emptyList()
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to fetch events", exception)
                _events.value = null
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun search(query: String) {
        if (query.isNotBlank()) {
            _searchResult.value = _events.value!!.filter { it.name.contains(query, true) }
        } else {
            _searchResult.value = _events.value!!
        }
    }
}
