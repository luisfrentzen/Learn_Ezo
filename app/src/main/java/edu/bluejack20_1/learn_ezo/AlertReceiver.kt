package edu.bluejack20_1.learn_ezo

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = NotificationHelper(context)

        var repeating_intent = Intent(context, MainActivity::class.java)
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        var pendingIntent = PendingIntent.getActivity(context, 1, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nb : NotificationCompat.Builder = notificationHelper.getChannelNotification().setContentIntent(pendingIntent)

        notificationHelper.getManager().notify(1, nb.build())
    }
}