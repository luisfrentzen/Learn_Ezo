package edu.bluejack20_1.learn_ezo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2

class ProblemActivity : AppCompatActivity() {
    lateinit var vpProblem : ViewPager2
    lateinit var user : Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem)

        vpProblem = findViewById(R.id.vp_problems)

        val arProblem = intent.getSerializableExtra("probs") as ArrayList<Problem>

        user = intent.getParcelableExtra<Player>("us") as Player

        vpProblem.adapter = ProblemPageAdapter(arProblem, user, this)
        vpProblem.isUserInputEnabled = false

        val btn_back = findViewById<ImageButton>(R.id.btn_back)
        btn_back.setOnClickListener {
            finish()
        }


        val ttl = findViewById<TextView>(R.id.prob_title)
        ttl.setText(intent.getStringExtra("title"))
    }

    fun nextPage(){
        vpProblem.setCurrentItem(vpProblem.currentItem + 1)
    }
}