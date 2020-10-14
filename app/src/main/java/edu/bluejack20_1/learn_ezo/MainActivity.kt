package edu.bluejack20_1.learn_ezo

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import edu.bluejack20_1.learn_ezo.LoginActivity


class MainActivity : AppCompatActivity() {
    lateinit var handler: Handler

    var databaseU : DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val acct = GoogleSignIn.getLastSignedInAccount(this)

            if (acct != null) {

                databaseU.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(data in snapshot.children){
                            if (data.key == acct.id){

                                val u = data.getValue(Player::class.java) as Player

                                startNavBottomActivity(u)

                                return
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }
        }
        else {
            handler = Handler()
            handler.postDelayed({
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }
    }

    private fun startNavBottomActivity(u: Player) {
        startActivity(NavBottom.getLaunchIntent(this, u))
        finish()
    }
}