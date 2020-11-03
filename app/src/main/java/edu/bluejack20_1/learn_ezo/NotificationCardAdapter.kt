package edu.bluejack20_1.learn_ezo

import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.notification_card.view.*
import kotlinx.android.synthetic.main.search_card.view.*
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class NotificationCardAdapter(val notification : ArrayList<SpannableStringBuilder>, val timestamp: ArrayList<Calendar>) : RecyclerView.Adapter<NotificationCardAdapter.ViewHolder>() {
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
        holder.acttv.text = notification.get(position)

        val cal = Calendar.getInstance()
        val timestm = timestamp.get(position)

        var diff = ""

        if(cal.get(Calendar.YEAR) - timestm.get(Calendar.YEAR) > 0){
            diff = (cal.get(Calendar.YEAR) - timestm.get(Calendar.YEAR)).toString() + " " + ctx.resources.getString(R.string.years_ago)
        }
        else if(cal.get(Calendar.MONTH) - timestm.get(Calendar.MONTH) > 0){
            diff = (cal.get(Calendar.MONTH) - timestm.get(Calendar.MONTH)).toString() + " " + ctx.resources.getString(R.string.months_ago)
        }
        else if(cal.get(Calendar.DAY_OF_MONTH) - timestm.get(Calendar.DAY_OF_MONTH) > 0){
            diff = (cal.get(Calendar.DAY_OF_MONTH) - timestm.get(Calendar.DAY_OF_MONTH)).toString() + " " + ctx.resources.getString(R.string.days_ago)
        }
        else if(cal.get(Calendar.HOUR_OF_DAY) - timestm.get(Calendar.HOUR_OF_DAY) > 0){
            diff = (cal.get(Calendar.HOUR_OF_DAY) - timestm.get(Calendar.HOUR_OF_DAY)).toString() + " " + ctx.resources.getString(R.string.hours_ago)
        }
        else if(cal.get(Calendar.MINUTE) - timestm.get(Calendar.MINUTE) > 0){
            diff = (cal.get(Calendar.MINUTE) - timestm.get(Calendar.MINUTE)).toString() + " " + ctx.resources.getString(R.string.minutes_ago)
        }
        else{
            diff = "1 " + ctx.resources.getString(R.string.minutes_ago)
        }
        holder.timetv.text = diff

    }

}