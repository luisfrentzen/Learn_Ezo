package edu.bluejack20_1.learn_ezo

import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


class LessonNodeAdapter(val lesson_list : ArrayList<Lesson>, var user : Player, ctx : Context) : RecyclerView.Adapter<LessonNodeAdapter.ViewHolder>(){

    val con : Context = ctx

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

        nodeBtn.setOnClickListener {
            val intent = Intent(con, LessonActivity::class.java)
            intent.putExtra("les", lesson)
            intent.putExtra("us", user)
            con.startActivity(intent)
        }

        nodeIcon.setImageResource(lesson.icon?.toInt() as Int)
        if(lesson.isCompleted == false){
            val matrix = ColorMatrix()
            matrix.setSaturation(0f)

            val filter = ColorMatrixColorFilter(matrix)

            nodeIcon.setColorFilter(filter)
            nodeIcon.alpha = 0.5f
            nodeBtn.isEnabled = false
        }
        else {
            nodeIcon.clearColorFilter()
            nodeIcon.alpha = 1f
            nodeBtn.isEnabled = true
        }

        textView.setText(lesson.title)
    }
}