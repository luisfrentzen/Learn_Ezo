package edu.bluejack20_1.learn_ezo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import edu.bluejack20_1.learn_ezo.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_nav.*
import kotlinx.android.synthetic.main.fragment_user.*


class NavBottom : AppCompatActivity() {
    lateinit var mGoogleSignInClient : GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

//    private fun loadFragment(fragment: Fragment?):Boolean{
//        if(fragment != null){
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fl_container, fragment)
//                .commit();
//            return true;
//        }
//
//        return false;
//    }

    fun signOut() {
        startActivity(LoginActivity.getLaunchIntent(this))
        Firebase.auth.signOut();
        mGoogleSignInClient.signOut()
    }

    private fun configureSignIn(){
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        configureSignIn()
//        loadFragment(Home())
        val viewPager : ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = MenuFragmentAdapter(this)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                btm_nav.getMenu().getItem(position).setChecked(true);

            }
        })

        btm_nav.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(p0: MenuItem): Boolean {

                when (p0.itemId){
                    R.id.home_menu -> {
                        //for see
                        viewPager.setCurrentItem(0)
                    }
                    R.id.practice_menu -> {
                        viewPager.setCurrentItem(1)
                    }
                    R.id.game_menu -> {
                        viewPager.setCurrentItem(2)
                    }
                    R.id.user_menu -> {
                        viewPager.setCurrentItem(3)
                    }
                }

                return true
            }

        })
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, NavBottom::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}