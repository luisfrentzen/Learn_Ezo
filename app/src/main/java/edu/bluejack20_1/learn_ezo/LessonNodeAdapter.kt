package edu.bluejack20_1.learn_ezo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LessonNodeAdapter(val lesson_list : ArrayList<Lesson>) : RecyclerView.Adapter<LessonNodeAdapter.ViewHolder>(){

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_lesson_title)
        val btnNode = itemView.findViewById<Button>(R.id.node_button)
        val ivIcon = itemView.findViewById<ImageView>(R.id.icon_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.lesson_node, parent, false)
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return lesson_list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson : Lesson = lesson_list.get(position)
        val textView = holder.tvTitle
        val nodeBtn = holder.btnNode
        val nodeIcon = holder.ivIcon

        nodeIcon.setImageResource(R.drawable.ic_trophy)

        textView.setText(lesson.lesson_title)
    }
}