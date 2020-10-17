package edu.bluejack20_1.learn_ezo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.*

class ReviewActivity : AppCompatActivity() {

    var arProb = ArrayList<Problem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        var review = intent.getStringExtra("review")?.toInt()

        var lessons = intent.getSerializableExtra("lessons") as ArrayList<Lesson>
        var lesson_mastered = intent.getStringExtra("lesson_mastered_count")?.toInt()

        val rom = ArrayList<String>()
        val alf = ArrayList<String>()
        val jpn = ArrayList<String>()

        if (lesson_mastered != null) {
            for(i in 0 until (lesson_mastered.toInt())){
                val databaseP : DatabaseReference = FirebaseDatabase.getInstance().getReference("lessons").child(lessons.get(i).id.toString()).child("pool")

                databaseP.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            alf.add(data.key.toString())

                            val temp = data.value.toString().split('-')
                            jpn.add(temp[0])
                            rom.add(temp[1])
                        }
                    }
                })

            }
        }

        if(review == 1){
            var tv_title : TextView = findViewById(R.id.title_review)

            tv_title.text = resources.getString(R.string.learning_review)

            var tv_desc : TextView = findViewById(R.id.review_desc)

            tv_desc.text = resources.getString(R.string.learning_review_desc)


            val startBtn = findViewById<Button>(R.id.start_button)

            startBtn.setOnClickListener {
                val intent = Intent(this, ProblemActivity::class.java)
                arProb = Problem.createProblems(20, alf, jpn, rom)
                intent.putExtra("probs", arProb)
                intent.putExtra("title", resources.getString(R.string.learning_review))
                intent.putExtra("lesson_id", "dummy1")
                intent.putExtra("us", Player(1.toString(), "dummy"))
                startActivity(intent)
                finish()
            }

        }else{
            var tv_title : TextView = findViewById(R.id.title_review)

            tv_title.text = resources.getString(R.string.quick_review)

            var tv_desc : TextView = findViewById(R.id.review_desc)

            tv_desc.text = resources.getString(R.string.quick_review_desc)

            val startBtn = findViewById<Button>(R.id.start_button)

            startBtn.setOnClickListener {
                val intent = Intent(this, ProblemActivity::class.java)
                arProb = Problem.createProblems(100, alf, jpn, rom)
                intent.putExtra("probs", arProb)
                intent.putExtra("title", resources.getString(R.string.learning_review))
                intent.putExtra("lesson_id", "dummy2")
                intent.putExtra("us", Player(1.toString(), "dummy"))
                startActivity(intent)
                finish()
            }
        }


        val backBtn = findViewById<ImageButton>(R.id.btn_back)

        backBtn.setOnClickListener {
            finish()
        }
    }
}
