/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.PendingIntentCompat
import androidx.core.content.getSystemService
import app.opass.ccip.android.MainActivity
import app.opass.ccip.android.receiver.SessionAlarmReceiver
import app.opass.ccip.network.models.schedule.Session

object AlarmUtil {

    const val INTENT_EXTRA_EVENT_ID = "INTENT_EXTRA_EVENT_ID"
    const val INTENT_EXTRA_SESSION_ID = "INTENT_EXTRA_SESSION_ID"
    const val INTENT_EXTRA_SESSION_TITLE = "INTENT_EXTRA_SESSION_TITLE"
    const val INTENT_EXTRA_SESSION_ROOM = "INTENT_EXTRA_SESSION_ROOM"

    private const val TAG = "AlarmUtil"

    fun setSessionAlarm(context: Context, startTime: Long, eventId: String, session: Session) {
        val intent = Intent(context, SessionAlarmReceiver::class.java).apply {
            action = "${eventId}_${session.id}"
            putExtra(INTENT_EXTRA_EVENT_ID, eventId)
            putExtra(INTENT_EXTRA_SESSION_ID, session.id)
            putExtra(INTENT_EXTRA_SESSION_TITLE, session.title)
            putExtra(INTENT_EXTRA_SESSION_ROOM, session.room)
        }

        val pendingIntent = PendingIntentCompat
            .getBroadcast(context, 0, intent, 0, false)!!

        Log.i(TAG, "Scheduling alarm for ${session.id} at $startTime")
        val alarmManager = context.getSystemService<AlarmManager>()!!
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            startTime - 10 * 60 * 1000,
            pendingIntent
        )
    }

    fun cancelSessionAlarm(context: Context, eventId: String, sessionId: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = "${eventId}_$sessionId"
        }

        val alarmManager = context.getSystemService<AlarmManager>()!!
        alarmManager.cancel(
            PendingIntent
                .getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        )
    }
}
