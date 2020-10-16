package edu.bluejack20_1.learn_ezo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProblemPageAdapter(private var problemList: ArrayList<Problem>) : RecyclerView.Adapter<ProblemPageAdapter.Pager2ViewHolder>(){

    inner class Pager2ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val rgChoices = itemView.findViewById<RadioGroup>(R.id.rg_choices)

        val problem = itemView.findViewById<TextView>(R.id.tv_problem)

    }

    override fun onCreateViewHolder(parent: ViewGroup,  viewType: Int): ProblemPageAdapter.Pager2ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.problem_layout, parent, false)
        return Pager2ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        val problem = problemList.get(position)
        holder.problem.setText(problem.problem)

        problem.choices.add(problem.ans)
        problem.choices.shuffle()

        (holder.rgChoices.getChildAt(1) as AppCompatRadioButton).setText(problem.choices.get(3))
        (holder.rgChoices.getChildAt(2) as AppCompatRadioButton).setText(problem.choices.get(0))
        (holder.rgChoices.getChildAt(3) as AppCompatRadioButton).setText(problem.choices.get(1))
        (holder.rgChoices.getChildAt(0) as AppCompatRadioButton).setText(problem.choices.get(2))

    }

    override fun getItemCount(): Int {
        return problemList.size
    }
}