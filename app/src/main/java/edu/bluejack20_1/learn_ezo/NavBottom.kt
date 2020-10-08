package edu.bluejack20_1.learn_ezo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

    private fun loadFragment(fragment: Fragment?):Boolean{
        if(fragment != null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit();
            return true;
        }

        return false;
    }

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
        loadFragment(Home())

        btm_nav.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
                var fragment: Fragment?=null

                when (p0.itemId){
                    R.id.home_menu -> {
                        //for see
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

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, NavBottom::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}