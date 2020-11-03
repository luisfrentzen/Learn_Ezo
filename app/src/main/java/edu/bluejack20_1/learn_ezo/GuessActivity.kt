package edu.bluejack20_1.learn_ezo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import java.util.*

class GuessActivity : AppCompatActivity() {
    lateinit var vpProblem : ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess)

        vpProblem = findViewById(R.id.vp_problems)

        val arProblem = intent.getSerializableExtra("probs") as ArrayList<Problem>
        val player = intent.getParcelableExtra<Player>("user")

        val tv_timer = findViewById<TextView>(R.id.timer)

        val databaseP : DatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(
            player?.id.toString()
        )

        Log.d("see user", player?.id.toString())
        Log.d("see user", databaseP.toString())


        object: CountDownTimer(60000, 1000){
            override fun onFinish() {
                finish()

                //add exp
                databaseP.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        var currExp : Int = snapshot.child("exp").value.toString().toInt()

                        databaseP.child("exp").setValue(currExp+10)
                    }

                })
            }

            override fun onTick(millisUntilFinished: Long) {
                tv_timer.text = ((millisUntilFinished / 1000).toInt()).toString()
            }

        }.start()

        vpProblem.adapter = GuessPageAdapter(arProblem, this)
        vpProblem.isUserInputEnabled = false

        val btn_back = findViewById<ImageButton>(R.id.btn_back)
        btn_back.setOnClickListener {
            finish()
        }
    }

    fun nextPage(){
        vpProblem.setCurrentItem(vpProblem.currentItem + 1)
    }
}