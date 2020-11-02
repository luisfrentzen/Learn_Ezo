package edu.bluejack20_1.learn_ezo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.ArrayList


class GuessIntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_intro)

        var databaseG : DatabaseReference = FirebaseDatabase.getInstance().getReference("guessImage")

        val link = ArrayList<String>()
        val jpn = ArrayList<String>()

        lateinit var arProblem : ArrayList<Problem>

        databaseG.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    jpn.add(data.key.toString())

                    link.add(data.value.toString())
                }

                arProblem = Problem.createGuessProblems(jpn, link)
            }

        })

        val startBtn = findViewById<Button>(R.id.start_btn)

        startBtn.setOnClickListener {
            val intent = Intent(this, GuessActivity::class.java)
            intent.putExtra("probs", arProblem)
            startActivity(intent)
            finish()
        }

        val backBtn = findViewById<ImageButton>(R.id.btn_back)

        backBtn.setOnClickListener{
            finish()
        }

    }
}