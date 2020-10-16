package edu.bluejack20_1.learn_ezo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SetupActivity : AppCompatActivity() {

    var u : Player? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        loadFragment(SetPreferencesFragment())

        u = intent.getParcelableExtra("user") as Player?
    }

    fun loadFragment(childFragment : Fragment){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_setup, childFragment).addToBackStack(null).commit()
    }

    fun finishSetup(){
        var databaseU : DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

        databaseU.child(u?.id as String).setValue(u as Player)
        startActivity(NavBottom.getLaunchIntent(this, u as Player))
    }

    companion object {
        fun getLaunchIntent(from: Context, loggedUser: Player) = Intent(from, SetupActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra("user", loggedUser as Parcelable)
        }
    }
}