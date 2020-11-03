package edu.bluejack20_1.learn_ezo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.database.*
import android.content.Intent

class MemorizeIntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memorize_intro)

        val btn_back = findViewById<ImageButton>(R.id.btn_back)
        btn_back.setOnClickListener {
            finish()
        }

        val database : DatabaseReference = FirebaseDatabase.getInstance().getReference("lessons")
        val dbhira = database.child("1").child("pool")
        val dbkata = database.child("2").child("pool")

        val pooljpn = ArrayList<String>()
        val poolrom = ArrayList<String>()

        var player : Player = intent.getParcelableExtra<Player>("user") as Player

        dbhira.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    val temp = data.value.toString().split('-')
                    val jpn = temp[0]
                    if(jpn.length < 2){
                        pooljpn.add(temp[0])
                        poolrom.add(temp[1])
                    }
                }
            }
        })

        dbkata.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    val temp = data.value.toString().split('-')
                    val jpn = temp[0]
                    if(jpn.length < 2){
                        pooljpn.add(temp[0])
                        poolrom.add(temp[1])
                    }
                }
            }
        })

        val btn = findViewById<Button>(R.id.start_btn)
        btn.setOnClickListener {
            val intent = Intent(this, MemorizeActivity::class.java)
            intent.putExtra("pooljpn", pooljpn)
            intent.putExtra("poolrom", poolrom)
            intent.putExtra("user", player)
            startActivity(intent)
            finish()
        }
    }


}