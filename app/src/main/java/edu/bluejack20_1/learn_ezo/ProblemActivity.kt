package edu.bluejack20_1.learn_ezo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2

class ProblemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem)

        val vpProblem : ViewPager2 = findViewById(R.id.vp_problems)

        val arProblem = ArrayList<Problem>()

        val arChoices = ArrayList<String>()
        arChoices.add("You")
        arChoices.add("Anda")
        arChoices.add("Engkau")
        arProblem.add(Problem("Apakah itu pantek", "Kau", arChoices))

        vpProblem.adapter = ProblemPageAdapter(arProblem)
    }
}