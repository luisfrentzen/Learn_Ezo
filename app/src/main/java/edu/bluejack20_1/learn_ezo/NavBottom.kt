package edu.bluejack20_1.learn_ezo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.os.Parcelable
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_nav.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log
import kotlin.properties.Delegates


class NavBottom : AppCompatActivity() {
    lateinit var mGoogleSignInClient : GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    var u : Player? = null

    var lastCompleted : Int ?= null

    val ach_list = ArrayList<Achievement>()

    lateinit var lessonAdapter : LessonNodeAdapter

    var lessons_mastered_count : Int = 0

    lateinit var userFragment : User

    var databaseL : DatabaseReference = FirebaseDatabase.getInstance().getReference("lessons")
    val lessons_list = ArrayList<Lesson>()

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

    fun moveToReviewPage(review : Int, player: Player){
        val intent = Intent(this, ReviewActivity::class.java)
        intent.putExtra("review", review.toString())
        intent.putExtra("lessons", lessons_list)
        intent.putExtra("user", player)
        intent.putExtra("lesson_mastered_count", lessons_mastered_count.toString())
        startActivity(intent)
    }

    fun moveToMemorizePage(){
        val intent = Intent(this, MemorizeIntroActivity::class.java)
        startActivity(intent)
    }

    fun moveToAchievementPage(ach_list : ArrayList<Achievement>, player : Player){
        val intent = Intent(this, AchievementActivity::class.java)
        intent.putExtra("ach", ach_list)
        intent.putExtra("user", player)
        startActivity(intent)
    }

    fun moveToFriendPage(){
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        configureSignIn()
//        loadFragment(Home())

        u = intent.getParcelableExtra("user") as Player?

        Log.d("desuu", u?.name.toString())

        Home.addUserRecord(u!!.id, 0)

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

        databaseA.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){

                    val u = data.getValue(Achievement::class.java) as Achievement

                    u.currentProgress = 0

                    var temp = resources.getIdentifier(u.icon, "drawable", packageName)

                    u.icon = temp.toString()

                    var temp_title = resources.getString(resources.getIdentifier(u.title, "string", packageName))

                    u.title = temp_title

                    var temp_desc = resources.getString(resources.getIdentifier(u.desc, "string", packageName))

                    u.desc = temp_desc

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

        databaseL.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for(data in snapshot.children){

                    val u = data.getValue(Lesson::class.java) as Lesson

                    val temp = resources.getIdentifier(u.icon, "drawable", packageName)

                    u.id = data.key?.toInt()
                    u.icon = temp.toString()

                    val temp_title = resources.getString(resources.getIdentifier(u.title, "string", packageName))

                    u.title = temp_title

                    val temp_lesson = resources.getString(resources.getIdentifier(u.short_lesson, "string", packageName))

                    u.short_lesson = temp_lesson

                    val temp_example = resources.getString(resources.getIdentifier(u.example, "string", packageName))

                    u.example = temp_example

                    lessons_list.add(u)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val databaseP : DatabaseReference = FirebaseDatabase.getInstance().getReference("accomplishment").child(
            acct?.id.toString()).child("lessons")

        databaseP.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                    val temp = snapshot.value.toString()

                Log.d("lesson", temp)

                if(temp == "null"){
                    var temp_lessons = lessons_list.get(0)

                    temp_lessons.isCompleted = true
                }else{
                    val temp_completed = temp.split(",")

                    lessons_mastered_count = temp_completed.size

                    var idx : Int = 0;
                    lastCompleted = temp_completed.get((temp_completed.lastIndex)).toInt().plus(1)

                    Log.d("lesson", lastCompleted.toString())

                    for(i in lessons_list){
                        i.isCompleted = idx < lastCompleted!!
                        idx = idx + 1
                    }
                }


                lessonAdapter.notifyDataSetChanged()

            }
        })

        lessonAdapter = LessonNodeAdapter(lessons_list, u!!,this)

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