package app.opass.ccip.android.ui.screens.ticket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import app.opass.ccip.android.ui.components.TopAppBar
import app.opass.ccip.android.ui.navigation.Screen

@Composable
fun Screen.Ticket.TicketScreen(
    navHostController: NavHostController,
    viewModel: TicketViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = stringResource(this.title),
                navHostController = navHostController
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) { }
    }
}
