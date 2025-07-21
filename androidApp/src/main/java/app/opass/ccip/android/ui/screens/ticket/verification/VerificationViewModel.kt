/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.ticket.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.android.ui.extensions.saveToken
import app.opass.ccip.android.ui.extensions.sharedPreferences
import app.opass.ccip.helpers.PortalHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = VerificationViewModel.Factory::class)
class VerificationViewModel @AssistedInject constructor(
    @ApplicationContext private val context: android.content.Context,
    private val portalHelper: PortalHelper,
    @Assisted("eventId") private val eventId: String,
    @Assisted("token") private val token: String
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("eventId") eventId: String, @Assisted("token") token: String): VerificationViewModel
    }

    private val _uiState = MutableStateFlow(VerificationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        verifyToken()
    }

    private fun verifyToken() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val attendee = portalHelper.getAttendee(eventId, token, true)
                if (attendee != null) {
                    val eventConfig = portalHelper.getEventConfig(eventId)
                    context.sharedPreferences.saveToken(eventId, token)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isVerified = true,
                            eventName = eventConfig.name,
                            userName = attendee.userId
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isVerified = false,
                            errorMessage = "Token is not valid"
                        )
                    }
                }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isVerified = false,
                        errorMessage = "Token is not valid"
                    )
                }
            }
        }
    }
}

data class VerificationUiState(
    val isLoading: Boolean = true,
    val isVerified: Boolean? = null, // null: not yet verified, true: success, false: failure
    val eventName: String? = null,
    val userName: String? = null,
    val errorMessage: String? = null
)
