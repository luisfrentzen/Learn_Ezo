package edu.bluejack20_1.learn_ezo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AchievementCardAdapter(val achievementList : ArrayList<Achievement>) :
    RecyclerView.Adapter<AchievementCardAdapter.ViewHolder>() {

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val achTitle = itemView.findViewById<TextView>(R.id.ach_title)
        val achDesc = itemView.findViewById<TextView>(R.id.ach_desc)
        val achIcon = itemView.findViewById<ImageView>(R.id.ach_icon)
        val achProg = itemView.findViewById<TextView>(R.id.progress)
        val achProgBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.achievement_card, parent, false)
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return achievementList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val achievement : Achievement = achievementList.get(position)
        val titleTextView = holder.achTitle
        val iconImageView = holder.achIcon
        val descTextView = holder.achDesc
        val progressView = holder.achProg
        val progressBarView = holder.achProgBar

        val prog = achievement.achievement_current_progress.toString() + "/" + achievement.achievement_target_proggress
        progressView.setText(prog)

        iconImageView.setImageResource(achievement.achievement_icon)
        progressBarView.max = achievement.achievement_target_proggress
        progressBarView.progress = achievement.achievement_current_progress

        titleTextView.setText(achievement.achievement_title)
        descTextView.setText(achievement.achievement_desc)
    }
}