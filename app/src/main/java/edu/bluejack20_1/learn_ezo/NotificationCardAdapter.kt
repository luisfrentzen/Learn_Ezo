package edu.bluejack20_1.learn_ezo

import android.content.Context
import android.provider.CalendarContract
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.notification_card.view.*
import kotlinx.android.synthetic.main.search_card.view.*
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class NotificationCardAdapter(val notification : ArrayList<Pair<SpannableStringBuilder, Calendar>>) : RecyclerView.Adapter<NotificationCardAdapter.ViewHolder>() {
    lateinit var ctx : Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationCardAdapter.ViewHolder {
        val context = parent.context
        ctx = context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.notification_card, parent, false)

        return ViewHolder(contactView)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val acttv = itemView.activity_tv
        val timetv = itemView.timestamp
    }

    override fun getItemCount(): Int {
        return notification.size
    }

    override fun onBindViewHolder(holder: NotificationCardAdapter.ViewHolder, position: Int) {
        holder.acttv.text = notification.get(position).first
        val notdate = notification.get(position).second

        val cal = Calendar.getInstance()
        val timestm = notdate

        var diff = ""

        val timenow: Long = ((cal.get(Calendar.DAY_OF_YEAR).toLong() + cal.get(Calendar.YEAR).toLong() * 365.toLong()) *24*60*60) + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND)
        val timethen: Long = ((timestm.get(Calendar.DAY_OF_YEAR).toLong() + timestm.get(Calendar.YEAR).toLong() * 365.toLong()) *24*60*60) + timestm.get(Calendar.MINUTE) * 60 + timestm.get(Calendar.SECOND)

        if(timenow - timethen < 60){
            //less than a minute
            diff = ctx.resources.getString(R.string.less_than_a_minute)
        }
        else if(timenow - timethen < 60 * 60){
            //less than an hour
            diff = ((timenow - timethen) / 60).toString() + " " + ctx.resources.getString(R.string.minutes_ago)
        }
        else if(timenow - timethen < 60 * 60 * 24){
            //less than a day
            diff = ((timenow - timethen) / ( 60*60)).toString() + " " + ctx.resources.getString(R.string.hours_ago)
        }
        else if(timenow - timethen < 60 * 60 * 24 * 30){
            //less than a month
            diff = ((timenow - timethen) / (60*60*24)).toString() + " " + ctx.resources.getString(R.string.days_ago)
        }
        else if(timenow - timethen < 60 * 60 * 24 * 365){
            //less than a month
            diff = ((timenow - timethen) / (60*60*24*30)).toString() + " " + ctx.resources.getString(R.string.months_ago)
        }

        holder.timetv.text = diff

    }

}