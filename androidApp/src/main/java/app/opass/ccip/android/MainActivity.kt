/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import app.opass.ccip.android.ui.extensions.currentEventId
import app.opass.ccip.android.ui.extensions.sharedPreferences
import app.opass.ccip.android.ui.navigation.Screen
import app.opass.ccip.android.ui.navigation.SetupNavGraph
import app.opass.ccip.android.ui.theme.OPassTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val currentEventId = sharedPreferences.currentEventId

        setContent {
            OPassTheme {
                val navController = rememberNavController()

                SetupNavGraph(
                    navHostController = navController,
                    startDestination = if (currentEventId.isNullOrBlank()) {
                        Screen.EventPreview
                    } else {
                        Screen.Event(currentEventId)
                    }
                )
            }
        }
    }
}
