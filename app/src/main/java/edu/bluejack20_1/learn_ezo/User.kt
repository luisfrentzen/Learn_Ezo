package edu.bluejack20_1.learn_ezo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat.canScrollVertically
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_user.*
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [User.newInstance] factory method to
 * create an instance of this fragment.
 */
class User : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_user, container, false)

        val acct = GoogleSignIn.getLastSignedInAccount(activity)

        val rvAchievement = root.findViewById<View>(R.id.rv_achievement) as RecyclerView

        var ach_list = (activity as NavBottom).getAchList()

        var ach_overview = ArrayList<Achievement>(ach_list.subList(0, 3))

        val rvAdapter = AchievementCardAdapter(ach_overview)
        rvAchievement.adapter = rvAdapter
        rvAchievement.hasFixedSize()
        rvAchievement.layoutManager = LinearLayoutManager(root.context)
        rvAchievement.isNestedScrollingEnabled = false

        val databaseO : DatabaseReference = FirebaseDatabase.getInstance().getReference("accomplishment").child(
            (activity as NavBottom).u?.id.toString()
        ).child("achievements")

        databaseO.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    val u = data.getValue(Achievement::class.java) as Achievement


                    var temp = ach_list.get(data.key?.toInt()?.minus(1) as Int)

                    temp.currentProgress = data.child("currentProgress").value.toString().toInt()

                    ach_list.set(data.key?.toInt()!!.minus(1) as Int, temp)

                    ach_overview = ArrayList<Achievement>(ach_list.subList(0, 3))

                    rvAdapter.notifyDataSetChanged()

                }
            }

        })

        val level_progress : ProgressBar = root.findViewById(R.id.level_progress)
        val leagueTv = root.findViewById<TextView>(R.id.tv_league)
        val day_streak_count : TextView = root.findViewById(R.id.day_streak_count)
        val exp_count : TextView = root.findViewById(R.id.exp_count)
        val tv_follower : TextView = root.findViewById(R.id.follower)
        val level_tv : TextView = root.findViewById(R.id.level_count)
        val tv_lesson_mastered : TextView = root.findViewById(R.id.tv_lesson_mastered)


        val databaseUser : DatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(
            (activity as NavBottom).u?.id.toString()
        )

        databaseUser.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val p : Player = snapshot.getValue(Player::class.java) as Player

                var temp_exp = p.exp
                val level_count = (temp_exp / 25) + 1
                temp_exp -= (level_count - 1) * 25

                if(level_count < 5){
                    leagueTv.text = resources.getString(R.string.lg_apprentice)
                }
                else if(level_count < 10){
                    leagueTv.text = resources.getString(R.string.lg_disciple)
                }
                else if(level_count < 25) {
                    leagueTv.text = resources.getString(R.string.lg_teacher)
                }
                else if(level_count < 50) {
                    leagueTv.text = resources.getString(R.string.lg_master)
                }
                else {
                    leagueTv.text = resources.getString(R.string.lg_scholar)
                }

                level_progress.progress = temp_exp

                day_streak_count.setText(p.dayStreak.toString())
                exp_count.text = p.exp.toString()

                tv_follower.setText( p.follower.toString().plus(" Follower / ").plus(p.following.toString()).plus(" Following"))

                level_tv.text = "Level ".plus(level_count)
            }

        })

        tv_lesson_mastered.text = (activity as NavBottom).lessons_mastered_count.toString()

        val databaseA : DatabaseReference = FirebaseDatabase.getInstance().getReference("accomplishment").child(
            (activity as NavBottom).u?.id.toString()
        )

        databaseA.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = snapshot.child("lessons").value.toString()

                val temp_completed = temp.split(",")

                tv_lesson_mastered.text = temp_completed.size.toString()
            }

        })


//        val btnFriend : Button = root.findViewById(R.id.btn_friend)
//        btnFriend.setOnClickListener {
//            (activity as NavBottom).moveToFriendPage()
//        }

        val btnSetting : ImageButton = root.findViewById(R.id.setting_button)
        btnSetting.setOnClickListener {
            val fragment : Profile = this@User.getParentFragment() as Profile
            fragment.loadFragment(Setting())
        }

        val activity : NavBottom = activity as NavBottom

        val btnAchievement : Button = root.findViewById(R.id.btn_achievements)
        btnAchievement.setOnClickListener {
            (activity as NavBottom?)?.moveToAchievementPage(ach_list, (activity as NavBottom).u as Player)
        }

        val personPhoto: Uri?
        val personName : String?
        val personEmail : String?
        val personId : String?

        val profilePic : ImageView = root.findViewById(R.id.profile_pic)


        if (acct != null) {
            personName = acct.givenName
            personEmail = acct.email
            personId = acct.id
            personPhoto = acct.photoUrl

            Glide.with(this).load(personPhoto).into(profilePic)

            val tv_dname : TextView = root.findViewById(R.id.display_name)
            tv_dname.setText(personName)


        }

//        val btnSetting : ImageButton = root.findViewById(R.id.setting_button)
//        btnSetting.setOnClickListener {
//            activity.changeViewPager(4)
//        }

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment User.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            User().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}