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

        var ctx = this


        object: CountDownTimer(60000, 1000){
            override fun onFinish() {
                finish()

                //add exp
                Home.setExp(ctx, 10)
                Home.addUserRecord(player?.id.toString(), 1, ctx)

                databaseP.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        var currExp : Int = snapshot.child("exp").value.toString().toInt()

                        databaseP.child("exp").setValue(currExp+10)
                        if (player != null) {
                            player.exp = currExp + 10
                        }

                        val databaseA : DatabaseReference = FirebaseDatabase.getInstance().getReference("accomplishment").child(
                            player?.id.toString()).child("achievements").child("2")

                        databaseA.addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {

                                val temp_snapshot = snapshot.value.toString()

                                if(temp_snapshot == "null"){
                                    databaseA.child("isComplete").setValue(0)
                                }
                                else if(snapshot.child("isComplete").value == 1){
                                    return
                                }

                                if((player?.exp?.div(25))?.plus(1)!! >= 5){
                                    databaseA.child("currentProgress").setValue(5)
                                    databaseA.child("isComplete").setValue(1)
                                    return
                                }

                                databaseA.child("currentProgress").setValue(player.exp/25+1)
                            }

                        })
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