package edu.bluejack20_1.learn_ezo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AchievementActivity : AppCompatActivity() {
    lateinit var listAchievement : ArrayList<Achievement>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement)

        listAchievement = intent.getSerializableExtra("ach") as ArrayList<Achievement>

        val rv_ach = findViewById<RecyclerView>(R.id.rv_ach_full)
        val rvAdapter = AchievementCardAdapter(listAchievement)
        rv_ach.adapter = rvAdapter
        rv_ach.hasFixedSize()
        rv_ach.layoutManager = LinearLayoutManager(this)

        val btn_back = findViewById<ImageButton>(R.id.btn_back)
        btn_back.setOnClickListener {
            finish()
        }
    }
}