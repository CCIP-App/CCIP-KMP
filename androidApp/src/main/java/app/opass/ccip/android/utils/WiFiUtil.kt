/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.utils

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import androidx.core.content.getSystemService
import app.opass.ccip.android.ui.extensions.createIntent
import app.opass.ccip.android.ui.extensions.createNetworkConfigurations
import app.opass.ccip.network.models.eventconfig.WiFi

object WifiUtil {

    fun installOrSuggestNetworks(context: Context, list: List<WiFi>) {
        if (!list.all { it.password.isNotBlank() }) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return context.startActivity(list.createIntent())
        } else {
            val manager = context.getSystemService<WifiManager>() ?: return
            @Suppress("DEPRECATION")
            list.createNetworkConfigurations().forEach {
                val networkId = manager.addNetwork(it)
                if (networkId != -1) {
                    manager.enableNetwork(networkId, false)
                }
            }
        }
    }
}
