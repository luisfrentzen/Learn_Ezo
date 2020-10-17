package edu.bluejack20_1.learn_ezo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.*

class ProblemActivity : AppCompatActivity() {
    lateinit var vpProblem : ViewPager2
    lateinit var user : Player
    lateinit var lesson_id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem)

        vpProblem = findViewById(R.id.vp_problems)

        val arProblem = intent.getSerializableExtra("probs") as ArrayList<Problem>

        user = intent.getParcelableExtra<Player>("us") as Player

        lesson_id = intent.getStringExtra("lesson_id") as String

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

    fun updateAccomplishment(){
        val databaseP : DatabaseReference = FirebaseDatabase.getInstance().getReference("accomplishment").child(
            user.id.toString()).child("lessons")

        databaseP.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                var temp = (snapshot.value as String).split(",")

                var flag = 0

                for(i in 0 until temp.size){

                    if(temp[i] == lesson_id) {
                        flag++
                        break
                    }
                }

                if(flag == 0){
                    var temp_string = snapshot.value as String

                    temp_string += "," + lesson_id

                    databaseP.setValue(temp_string)
                }

//                var temp : String = snapshot.value as String
//
//                temp.plus(",").plus(lesson_id)
//
//
//                databaseP.setValue(temp)
            }

        })
    }

}