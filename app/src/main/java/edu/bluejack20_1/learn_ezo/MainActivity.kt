package edu.bluejack20_1.learn_ezo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
        val sharedPreferences: SharedPreferences? = getSharedPreferences(
            "sharedPrefs", Context.MODE_PRIVATE
        )

        val isDarkModeOn = sharedPreferences
            ?.getBoolean(
                "isDarkModeOn", false
            )

        if (isDarkModeOn!!) {
            AppCompatDelegate
                .setDefaultNightMode(
                    AppCompatDelegate
                        .MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate
                .setDefaultNightMode(
                    AppCompatDelegate
                        .MODE_NIGHT_NO);
        }

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