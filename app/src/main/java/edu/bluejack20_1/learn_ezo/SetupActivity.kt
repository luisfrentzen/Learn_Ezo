package edu.bluejack20_1.learn_ezo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class SetupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        loadFragment(SetPreferencesFragment())
    }

    fun loadFragment(childFragment : Fragment){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_setup, childFragment).addToBackStack(null).commit()
    }

    fun finishSetup(){

    }
}