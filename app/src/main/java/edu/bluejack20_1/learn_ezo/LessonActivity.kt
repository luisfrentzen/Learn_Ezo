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
import org.w3c.dom.Text

class LessonActivity : AppCompatActivity() {
    lateinit var lesson : Lesson
    lateinit var user : Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        val startBtn = findViewById<Button>(R.id.start_button)

        lesson = intent.getSerializableExtra("les") as Lesson
        user = intent.getParcelableExtra<Player>("us") as Player

        Log.d("desu", user.name.toString())

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

                arProb = Problem.createProblems(10, alf, jpn, rom)
            }
        })

        val ttl = findViewById<TextView>(R.id.title_lesson)
        ttl.setText(lesson.title)

        val title_container = findViewById<TextView>(R.id.tv_title)
        title_container.setText(lesson.title)

        val tv_short_lesson = findViewById<TextView>(R.id.tv_short_lesson)
        tv_short_lesson.setText(lesson.short_lesson)

        val tv_example = findViewById<TextView>(R.id.tv_example)
        tv_example.setText(lesson.example)

        startBtn.setOnClickListener {
            val intent = Intent(this, ProblemActivity::class.java)
            intent.putExtra("probs", arProb)
            intent.putExtra("title", ttl.text)
            intent.putExtra("lesson_id", lesson.id.toString())
            intent.putExtra("us", user)
            startActivity(intent)
            finish()
        }
    }



}