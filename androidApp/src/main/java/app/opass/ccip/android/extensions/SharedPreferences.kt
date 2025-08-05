/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.extensions

import android.content.SharedPreferences
import androidx.core.content.edit

private const val CURRENT_EVENT_ID = "CURRENT_EVENT_ID"
private const val TOKEN = "TOKEN"
private const val AUTO_BRIGHTEN = "AUTO_BRIGHTEN"
private const val BOOKMARK = "BOOKMARK"
private const val ALARMS = "ALARMS"

val SharedPreferences.autoBrighten: Boolean
    get() = this.getBoolean(AUTO_BRIGHTEN, true)

val SharedPreferences.currentEventId: String?
    get() = this.getString(CURRENT_EVENT_ID, null)

fun SharedPreferences.saveCurrentEventId(eventId: String) {
    return this.edit { putString(CURRENT_EVENT_ID, eventId) }
}

fun SharedPreferences.getToken(eventId: String): String? {
    return this.getString("${eventId}_$TOKEN", null)
}

fun SharedPreferences.saveToken(eventId: String, token: String) {
    return this.edit { putString("${eventId}_$TOKEN", token) }
}

fun SharedPreferences.removeToken(eventId: String) {
    return this.edit { remove("${eventId}_$TOKEN") }
}

fun SharedPreferences.autoBrighten(enabled: Boolean) {
    return this.edit { putBoolean(AUTO_BRIGHTEN, enabled) }
}

fun SharedPreferences.getBookmarks(eventId: String): Set<String> {
    return this.getStringSet("${eventId}_$BOOKMARK", emptySet()) ?: emptySet()
}

fun SharedPreferences.saveBookmarks(eventId: String, sessionIDs: Set<String>) {
    return this.edit { putStringSet("${eventId}_$BOOKMARK", sessionIDs) }
}

fun SharedPreferences.getAlarms(eventId: String): Set<String> {
    return this.getStringSet("${eventId}_$ALARMS", emptySet()) ?: emptySet()
}

fun SharedPreferences.saveAlarms(eventId: String, sessionIDs: Set<String>) {
    return this.edit { putStringSet("${eventId}_$ALARMS", sessionIDs) }
}
