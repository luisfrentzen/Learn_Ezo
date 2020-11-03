package edu.bluejack20_1.learn_ezo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = NotificationHelper(context)
        val nb : NotificationCompat.Builder = notificationHelper.getChannelNotification()
        notificationHelper.getManager().notify(1, nb.build())
    }
}