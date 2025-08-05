/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.extensions

import android.content.Intent
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import app.opass.ccip.network.models.eventconfig.WiFi

@RequiresApi(Build.VERSION_CODES.R)
fun List<WiFi>.createIntent(): Intent {
    return Intent(Settings.ACTION_WIFI_ADD_NETWORKS).apply {
        putParcelableArrayListExtra(
            Settings.EXTRA_WIFI_NETWORK_LIST,
            ArrayList(this@createIntent.createNetworkSuggestions())
        )
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
private fun List<WiFi>.createNetworkSuggestions(): List<WifiNetworkSuggestion> {
    return this.map { wifi ->
        WifiNetworkSuggestion.Builder()
            .setSsid(wifi.SSID.wrapInQuotes())
            .setWpa2Passphrase(wifi.password.wrapInQuotes())
            .build()
    }
}

@Suppress("Deprecation")
fun List<WiFi>.createNetworkConfigurations(): List<WifiConfiguration> {
    return this.map { wifi ->
        WifiConfiguration().apply {
            SSID = wifi.SSID.wrapInQuotes()
            preSharedKey = wifi.password.wrapInQuotes()
        }
    }
}
