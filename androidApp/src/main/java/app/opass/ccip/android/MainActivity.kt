/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import app.opass.ccip.android.ui.extensions.currentEventId
import app.opass.ccip.android.ui.extensions.sharedPreferences
import app.opass.ccip.android.ui.navigation.Screen
import app.opass.ccip.android.ui.navigation.NavGraph
import app.opass.ccip.android.ui.theme.OPassTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val currentEventId = sharedPreferences.currentEventId

        setContent {
            OPassTheme {
                val navController = rememberNavController()

                NavGraph(
                    navHostController = navController,
                    startDestination = if (currentEventId.isNullOrBlank()) {
                        Screen.Preview
                    } else {
                        Screen.Event(currentEventId)
                    }
                )
            }
        }
    }
}
