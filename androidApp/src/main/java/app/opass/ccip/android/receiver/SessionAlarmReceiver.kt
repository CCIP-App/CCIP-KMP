/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import app.opass.ccip.android.utils.AlarmUtil.INTENT_EXTRA_EVENT_ID
import app.opass.ccip.android.utils.AlarmUtil.INTENT_EXTRA_SESSION_ID
import app.opass.ccip.android.utils.AlarmUtil.INTENT_EXTRA_SESSION_ROOM
import app.opass.ccip.android.utils.AlarmUtil.INTENT_EXTRA_SESSION_TITLE
import app.opass.ccip.android.utils.NotificationUtil

class SessionAlarmReceiver: BroadcastReceiver() {

    private val TAG = SessionAlarmReceiver::class.java.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val eventId = intent.getStringExtra(INTENT_EXTRA_EVENT_ID) ?: return
        val sessionId = intent.getStringExtra(INTENT_EXTRA_SESSION_ID) ?: return
        val title = intent.getStringExtra(INTENT_EXTRA_SESSION_TITLE) ?: return
        val room = intent.getStringExtra(INTENT_EXTRA_SESSION_ROOM) ?: return

        val notificationManager = context.getSystemService<NotificationManager>()!!
        val id = "${eventId}_${sessionId}".hashCode()
        val notification = NotificationUtil.getSessionNotification(
            context, eventId, sessionId, title, room
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(id, notification)
            } else {
                Log.i(TAG, "Received alarm but notifications are disabled!")
            }
        } else {
            notificationManager.notify(id, notification)
        }
    }
}
