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

    fun changeViewPager(fragment: Int){
        if(fragment == 4){
            viewPager.setCurrentItem(fragment, false)
        }else{
            viewPager.setCurrentItem(fragment)
        }

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
                if(position == 4) {
                    super.onPageSelected(3)
                    btm_nav.getMenu().getItem(3).setChecked(true);
                }else{
                    super.onPageSelected(position)
                    btm_nav.getMenu().getItem(position).setChecked(true);
                }
            }
        })

        btm_nav.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
                when (p0.itemId){
                    R.id.home_menu -> {
                        //for see
                        changeViewPager(0)
                    }
                    R.id.practice_menu -> {
                        changeViewPager(1)
                    }
                    R.id.game_menu -> {
                        changeViewPager(2)
                    }
                    R.id.user_menu -> {
                        changeViewPager(3)
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