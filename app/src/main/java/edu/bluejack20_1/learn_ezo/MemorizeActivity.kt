package edu.bluejack20_1.learn_ezo

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.wajahatkarim3.easyflipview.EasyFlipView
import com.wajahatkarim3.easyflipview.EasyFlipView.OnFlipAnimationListener
import kotlinx.android.synthetic.main.activity_memorize.*
import kotlinx.android.synthetic.main.fragment_reminder_preferences.*
import kotlin.random.Random

class MemorizeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memorize)

        val backLayouts = ArrayList<View>()
        val flipViews = ArrayList<EasyFlipView>()

        val player : Player = intent.getParcelableExtra<Player>("user") as Player

        object: CountDownTimer(90000, 1000){
            override fun onFinish() {
                finish()
            }

            override fun onTick(millisUntilFinished: Long) {
                game_timer.text = ((millisUntilFinished / 1000).toInt()).toString()
            }

        }.start()

        var flipped = 0

        var pooljpn = intent.getStringArrayListExtra("pooljpn")
        var poolrom = intent.getStringArrayListExtra("poolrom")



        btn_backMem.setOnClickListener{
            finish()
        }

        val seed : Long = System.nanoTime()
        pooljpn!!.shuffle(Random(seed))
        poolrom!!.shuffle(Random(seed))

        val pool = ArrayList<String>()
        var score = 0

        pool.addAll(pooljpn.take(10))
        pool.addAll(poolrom.take(10))

        pool.shuffle()

        val flipidx = IntArray(4)
        var isCorrect = false

        for( i in 1 until 21) {
            val str = "back_layout" + i
            val str2 = "fv_" + i
            backLayouts.add(findViewById(resources.getIdentifier(str, "id", packageName)))
            val fv = findViewById<EasyFlipView>(resources.getIdentifier(str2, "id", packageName))
            fv.setOnFlipListener(OnFlipAnimationListener { flipView, newCurrentSide ->
//                Log.d("fv id", i.toString())

                if(newCurrentSide.toString() == "BACK_SIDE"){
                    flipView.isFlipOnTouch = false
                    flipidx[flipped] = i
                    flipped++

                    if(flipped == 2){


                        var a = pooljpn.indexOf(backLayouts.get(flipidx[0] - 1).findViewById<TextView>(R.id.back_value).text)

                        var idx1 = if ( a == -1 ){
                            poolrom.indexOf(backLayouts.get(flipidx[0] - 1).findViewById<TextView>(R.id.back_value).text)
                        }
                        else {
                            a
                        }

                        var b = pooljpn.indexOf(backLayouts.get(flipidx[1] - 1).findViewById<TextView>(R.id.back_value).text)

                        var idx2 = if ( b == -1 ){
                            poolrom.indexOf(backLayouts.get(flipidx[1] - 1).findViewById<TextView>(R.id.back_value).text)
                        }
                        else {
                            b
                        }

                        if( idx1 == idx2 ){
                            isCorrect = true
                        }

                        val fv1 = flipViews.get(flipidx[0] - 1)

                        fv1.flipTheView()
                        if(isCorrect){
                            fv1.visibility = View.INVISIBLE
                        }
                        fv1.isFlipOnTouch = true

                    }
                }
                else {
                    if(flipped != 0){
                        flipped = 0

                        val fv2 = flipViews.get(flipidx[1] - 1)
                        fv2.flipTheView()
                        if(isCorrect){
                            fv2.visibility = View.INVISIBLE
                            score++
                            if(score == 10){
                                winGame(player)
                            }
                        }
                        fv2.isFlipOnTouch = true

                        isCorrect = false

                        Log.d("flip", ("" + flipidx[0] + " " + flipidx[1]))
                    }
                }
            })

            flipViews.add(fv)

        }

        var idx = 0

        for( view in backLayouts ){
            val tv = view.findViewById<TextView>(R.id.back_value)
            tv.text = pool.get(idx)
            idx++
        }
    }

    fun winGame(player: Player){
        finish()

        addExp(player)
    }

    fun addExp(player: Player){
        val databaseP : DatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(
            player.id.toString()
        )

        databaseP.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var currExp : Int = snapshot.child("exp").value.toString().toInt()

                databaseP.child("exp").setValue(currExp+10)
                player.exp += currExp + 10

                val databaseA : DatabaseReference = FirebaseDatabase.getInstance().getReference("accomplishment").child(
                    player.id.toString()).child("achievements").child("2")

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

                        if(player.exp/25+1 >= 5){
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

}