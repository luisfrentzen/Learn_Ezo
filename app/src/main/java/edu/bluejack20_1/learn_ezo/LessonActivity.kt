package edu.bluejack20_1.learn_ezo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.*

class LessonActivity : AppCompatActivity() {
    lateinit var lesson : Lesson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        val startBtn = findViewById<Button>(R.id.start_button)

        lesson = intent.getSerializableExtra("les") as Lesson

        val tvShortLesson = findViewById<TextView>(R.id.tv_short_lesson)
        tvShortLesson.setText(lesson.short_lesson)

        val databaseP : DatabaseReference = FirebaseDatabase.getInstance().getReference("lessons").child(lesson.id.toString()).child("pool")

        val rom = ArrayList<String>()
        val alf = ArrayList<String>()
        val jpn = ArrayList<String>()

        lateinit var arProb : ArrayList<Problem>

        databaseP.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    alf.add(data.key.toString())

                    val temp = data.value.toString().split('-')
                    jpn.add(temp[0])
                    rom.add(temp[1])
                }

                arProb = createProblems(alf, jpn, rom)
            }
        })

        val ttl = findViewById<TextView>(R.id.title_lesson)
        ttl.setText(lesson.title)

        startBtn.setOnClickListener {
            val intent = Intent(this, ProblemActivity::class.java)
            intent.putExtra("probs", arProb)
            intent.putExtra("title", ttl.text)
            startActivity(intent)
            finish()
        }
    }

    fun createProblems(alf : ArrayList<String>, jpn : ArrayList<String>, rom : ArrayList<String>) : ArrayList<Problem>{
        //tipe soal 1 = alf - jpn
        //tipe soal 2 = jpn - alf
        //tipe soal 3 = jpn - rom

        val arProb = ArrayList<Problem>()

        for( i in 0 until 10 ){
            val type : Int = (1..3).random()

            Log.d("type", type.toString())

            val maxSize : Int = alf.size
            val idx : Int = (0..maxSize-1).random()

            lateinit var prob : String
            lateinit var ans : String
            lateinit var choices : ArrayList<String>

            lateinit var temp : ArrayList<Problem>

            when(type){
                1 -> {
                    prob = alf.get(idx)
                    ans = jpn.get(idx)

                    temp = jpn.toMutableList() as ArrayList<Problem>
                    temp.removeAt(idx)
                    temp.shuffle()

                }
                2 -> {
                    prob = jpn.get(idx)
                    ans = alf.get(idx)

                    temp = alf.toMutableList() as ArrayList<Problem>
                    temp.removeAt(idx)
                    temp.shuffle()

                }
                3 -> {
                    prob = jpn.get(idx)
                    ans = rom.get(idx)

                    temp = rom.toMutableList() as ArrayList<Problem>
                    temp.removeAt(idx)
                    temp.shuffle()

                }
            }

            choices = temp.take(3) as ArrayList<String>

            choices.add(ans)
            choices.shuffle()

            arProb.add(Problem(prob, ans, choices))

        }

        return arProb
    }

}