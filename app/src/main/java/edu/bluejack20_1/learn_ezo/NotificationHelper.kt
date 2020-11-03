package edu.bluejack20_1.learn_ezo

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class NotificationHelper : ContextWrapper{

    val channelID : String = "channel1ID"
    val channelName : String = "Channel 1"

    var mManager : NotificationManager? = null

    constructor(base: Context) : super(base) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel()
//            Log.d("masuk sini", "masuk kondisi")
        }
//        Log.d("masuk sini", "lalala")
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun createChannel(){
        val channel1  = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)


        getManager().createNotificationChannel(channel1)

    }

    fun getManager() : NotificationManager {
        if(mManager == null){
            mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        return mManager as NotificationManager
    }

    fun getChannelNotification() : NotificationCompat.Builder{
        return NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle("Daily Reminder~")
            .setContentText("Don't forget to learn today!")
            .setSmallIcon(R.drawable.ic_maskotmobile)
    }
}