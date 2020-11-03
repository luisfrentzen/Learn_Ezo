package edu.bluejack20_1.learn_ezo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AchievementActivity : AppCompatActivity() {
    lateinit var listAchievement : ArrayList<Achievement>
    lateinit var user : Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement)

        listAchievement = intent.getSerializableExtra("ach") as ArrayList<Achievement>
        user = intent.getParcelableExtra<Player>("user") as Player

        val rv_ach = findViewById<RecyclerView>(R.id.rv_ach_full)
        val rvAdapter = AchievementCardAdapter(listAchievement)
        rv_ach.adapter = rvAdapter
        rv_ach.hasFixedSize()
        rv_ach.layoutManager = LinearLayoutManager(this)

        val databaseO : DatabaseReference = FirebaseDatabase.getInstance().getReference("accomplishment").child(
            user.id.toString()
        ).child("achievements")

        val total_completed = findViewById<TextView>(R.id.total_completed_count)


        databaseO.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var count = 0
                for(data in snapshot.children){
                    val u = data.getValue(Achievement::class.java) as Achievement

                    var temp = listAchievement.get(data.key?.toInt()?.minus(1) as Int)

                    if(data.child("currentProgress").value.toString() == "null"){
                        temp.currentProgress = 0
                    }
                    temp.currentProgress = data.child("currentProgress").value.toString().toInt()

                    listAchievement.set(data.key?.toInt()!!.minus(1) as Int, temp)

                    rvAdapter.notifyDataSetChanged()

                    if(data.child("isComplete").value.toString() == "1"){
                        count++
                    }

                }
                total_completed.setText(count.toString())
            }

        })

        val btn_back = findViewById<ImageButton>(R.id.btn_back)
        btn_back.setOnClickListener {
            finish()
        }
    }
}