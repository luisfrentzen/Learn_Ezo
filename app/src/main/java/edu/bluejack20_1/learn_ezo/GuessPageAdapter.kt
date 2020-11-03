package edu.bluejack20_1.learn_ezo

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*

class GuessPageAdapter(private var problemList: ArrayList<Problem>, var act : Context) : RecyclerView.Adapter<GuessPageAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val view = itemView

        val rgChoices = itemView.findViewById<RadioGroup>(R.id.rg_choices)
        val nextBtn = itemView.findViewById<Button>(R.id.check_btn)

        var arChoicesBtn = ArrayList<CustomRadio>()

        val problem = itemView.findViewById<ImageView>(R.id.iv_problem)

        fun populateArChoices(){
            arChoicesBtn.add(itemView.findViewById<CustomRadio>(R.id.option1))
            arChoicesBtn.add(itemView.findViewById<CustomRadio>(R.id.option2))
            arChoicesBtn.add(itemView.findViewById<CustomRadio>(R.id.option3))
            arChoicesBtn.add(itemView.findViewById<CustomRadio>(R.id.option4))

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuessPageAdapter.Pager2ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.guess_layout, parent, false)
        val pager : GuessPageAdapter.Pager2ViewHolder = Pager2ViewHolder((contactView))
        pager.populateArChoices()


        return pager
    }

    override fun onBindViewHolder(holder: GuessPageAdapter.Pager2ViewHolder, position: Int) {
        val problem = problemList.get(position)

        Glide.with(act)
            .load(Uri.parse(problem.problem))
            .into(holder.problem)

        holder.rgChoices.clearCheck()
        val options = holder.arChoicesBtn
        val dr : Drawable? = act.getDrawable(R.drawable.btn_rounded)

        for(i in options){
            i.setBackgroundResource(R.drawable.selectable_choices_background)
            i.isChecked = false
            i.setOnClickListener {
                holder.nextBtn.isEnabled = true
                dr!!.mutate()
                dr.alpha = 255
            }
        }

        holder.nextBtn.text = act.getString(R.string.check)
        holder.nextBtn.isEnabled = false
        dr!!.mutate()
        dr.alpha = 179

        holder.nextBtn.setOnClickListener {
            val selectedBtn = holder.view.findViewById<CustomRadio>(holder.rgChoices.checkedRadioButtonId)

            if( selectedBtn.text == problem.ans){
                selectedBtn.setBackgroundResource(R.drawable.correct_choice)
            }
            else if( selectedBtn.text != problem.ans) {
                selectedBtn.setBackgroundResource(R.drawable.wrong_choice)

                for(btn in holder.arChoicesBtn) {
                    if ( btn.text == problem.ans) {
                        btn.setBackgroundResource(R.drawable.correct_choice)
                        break
                    }
                }
            }

            if(position == itemCount - 1){
                holder.nextBtn.text = act.getString(R.string.finish)
                holder.nextBtn.setOnClickListener {
                    (act as GuessActivity).finish()

                }
            }
            else {
                holder.nextBtn.text = act.getString(R.string.next)
                holder.nextBtn.setOnClickListener {
                    (act as GuessActivity).nextPage()
                }
            }
        }

        (holder.rgChoices.getChildAt(1) as AppCompatRadioButton).setText(problem.choices.get(3))
        (holder.rgChoices.getChildAt(2) as AppCompatRadioButton).setText(problem.choices.get(0))
        (holder.rgChoices.getChildAt(3) as AppCompatRadioButton).setText(problem.choices.get(1))
        (holder.rgChoices.getChildAt(0) as AppCompatRadioButton).setText(problem.choices.get(2))

    }

    override fun getItemCount(): Int {
        return problemList.size
    }
}