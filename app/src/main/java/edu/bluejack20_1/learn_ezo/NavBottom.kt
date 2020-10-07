package edu.bluejack20_1.learn_ezo

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import edu.bluejack20_1.learn_ezo.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_nav.*


class NavBottom : AppCompatActivity() {

    private fun loadFragment(fragment: Fragment?):Boolean{
        if(fragment != null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit();
            return true;
        }

        return false;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)

        loadFragment(Home())

        btm_nav.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
                var fragment: Fragment?=null

                when (p0.itemId){
                    R.id.home_menu -> {
                        fragment = Home()
                    }
                    R.id.practice_menu -> {
                        fragment = Practice()
                    }
                    R.id.game_menu -> {
                        fragment = Game()
                    }
                    R.id.user_menu -> {
                        fragment = User()
                    }
                }

                loadFragment(fragment)

                return true
            }

        })
    }
}