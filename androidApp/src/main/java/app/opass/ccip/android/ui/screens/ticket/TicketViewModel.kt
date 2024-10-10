package app.opass.ccip.android.ui.screens.ticket

import androidx.lifecycle.ViewModel
import app.opass.ccip.helpers.PortalHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val portalHelper: PortalHelper
): ViewModel() {

}
