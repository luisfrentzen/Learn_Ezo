package edu.bluejack20_1.learn_ezo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.os.Parcelable
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_nav.*
import java.util.*
import kotlin.collections.ArrayList


class NavBottom : AppCompatActivity() {
    lateinit var mGoogleSignInClient : GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    var u : Player? = null
    var databaseR : DatabaseReference = FirebaseDatabase.getInstance().getReference("records")
    val ach_list = ArrayList<Achievement>()

    fun signOut() {
        startActivity(LoginActivity.getLaunchIntent(this))
        Firebase.auth.signOut();
        mGoogleSignInClient.signOut()
    }

    fun getAchList() : ArrayList<Achievement>{
        return ach_list
    }

    private fun configureSignIn(){
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    fun moveToAchievementPage(ach_list : ArrayList<Achievement>){
        val intent = Intent(this, AchievementActivity::class.java)
        intent.putExtra("ach", ach_list)
        startActivity(intent)
    }

    fun moveToFriendPage(){
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }


    private fun addUserRecord(id: String){

        val cal : Calendar = Calendar.getInstance()

        val day : String = cal.get(Calendar.DATE).toString()
        val month : String = cal.get(Calendar.MONTH).toString()
        val year : String = cal.get(Calendar.YEAR).toString()

        val date : String = day.plus("-").plus(month).plus("-").plus(year)

        databaseR.child(id).child(date).setValue(0)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        configureSignIn()
//        loadFragment(Home())

        u = intent.getParcelableExtra("user") as Player?

        addUserRecord(u!!.id)

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

        val acct = GoogleSignIn.getLastSignedInAccount(this)

        var databaseA : DatabaseReference = FirebaseDatabase.getInstance().getReference("achievements")

        databaseA.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){

                    val u = data.getValue(Achievement::class.java) as Achievement

                    u.currentProgress = 0

                    var temp = resources.getIdentifier(u.icon, "drawable", packageName)

                    u.icon = temp.toString()

                    val databaseP : DatabaseReference = FirebaseDatabase.getInstance().getReference("accomplishment").child(
                        acct?.id.toString()).child("achievements").child(u.id.toString())

                    databaseP.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val temp = (snapshot.child("currentProgress").value).toString()

                            if(!temp.equals("null")){
                                u.currentProgress = temp.toInt()
                            }

                            ach_list.add(u)
                        }

                    })


                }


            }

            override fun onCancelled(error: DatabaseError) {

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
        fun getLaunchIntent(from: Context, loggedUser: Player) = Intent(from, NavBottom::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra("user", loggedUser as Parcelable)
        }
    }
}