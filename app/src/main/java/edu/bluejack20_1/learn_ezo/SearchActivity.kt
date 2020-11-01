package edu.bluejack20_1.learn_ezo

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : AppCompatActivity() {

    val db = FirebaseDatabase.getInstance()
    lateinit var editText : EditText

    var result : ArrayList<Player> = ArrayList<Player>();

    lateinit var p : Player;
    lateinit var rvAdapter : SearchCardAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        p = intent.getParcelableExtra<Player>("user") as Player;

        val btn_back = findViewById<ImageButton>(R.id.btn_back)
        btn_back.setOnClickListener {
            finish()
        }

        val following_list = ArrayList<String>()

        val ref = db.getReference("users").child(p.id.toString()).child("following-list")

        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for ( data in snapshot.children){
                    following_list.add(data.key.toString())
                }
            }

        })

        editText = findViewById<EditText>(R.id.search_edit_text)

        editText.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && editText.text.toString().length != 0) {
                result.clear();
                searchPeople(editText.text.toString())
                return@OnEditorActionListener true
            }
            false
        })

        rvAdapter = SearchCardAdapter(result, p, following_list)

        result_rv.adapter = rvAdapter
        result_rv.hasFixedSize()
        result_rv.layoutManager = LinearLayoutManager(this)
    }

    fun searchPeople(str : String){
        val ref = db.getReference("users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                for(data in snapshot.children){
                    val u = data.getValue(Player::class.java) as Player

                    if(u.name.toLowerCase().contains(str.toLowerCase()) && !u.name.equals(p.name)){
                        result.add(u);
                    }
                }

                rvAdapter.notifyDataSetChanged()
            }

        })
    }


}