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


//        ach_list.add(Achievement(1, "Hardworker", "Reach 10 days streak", 10, R.drawable.ic_ach_hardworker))
//        ach_list.add(Achievement(2, "Intermediate Student", "Reach level 5", 5, R.drawable.ic_ach_intermediate))
//        ach_list.add(Achievement(3, "Conqueror", "Complete all lessons", 6, R.drawable.ic_ach_conqueror))
//        ach_list.add(Achievement(4, "Social Person", "Follow 3 friends", 3, R.drawable.ic_ach_social))
//        ach_list.add(Achievement(5, "Big Brain", "Complete 3 reviews", 3, R.drawable.ic_ach_bigbrain))
//        ach_list.add(Achievement(6, "Popular One", "Have 3 followers", 3, R.drawable.ic_ach_popular))
//        ach_list.add(Achievement(7, "Genius", "Complete a lesson with no mistake", 1, R.drawable.ic_ach_genius))

        val rvAchievement = root.findViewById<View>(R.id.rv_achievement) as RecyclerView
        var ach_list = ArrayList<Achievement>()
        var ach_icon_list = ArrayList<Int>()

        var databaseA : DatabaseReference = FirebaseDatabase.getInstance().getReference("achievements")

        Log.d("desu", databaseA.toString())

        databaseA.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){

                    val u = data.getValue(Achievement::class.java) as Achievement

                    u.currentProgress = 0

                    var temp = resources.getIdentifier(u.icon, "drawable", context?.packageName)

                    u.icon = temp.toString()

                    val databaseP : DatabaseReference = FirebaseDatabase.getInstance().getReference("accomplishment").child(
                    acct?.id.toString()).child("achievements").child(u.id.toString())

                    databaseP.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            var temp = (snapshot.child("currentProgress").value).toString()

                            if(!temp.equals("null")){
                                u.currentProgress = temp.toInt()
                            }

                        }

                    })

                    ach_list.add(u)

                }

                var ach_overview = ArrayList<Achievement>(ach_list.subList(0, 3))

                val rvAdapter = AchievementCardAdapter(ach_overview)
                rvAchievement.adapter = rvAdapter
                rvAchievement.hasFixedSize()
                rvAchievement.layoutManager = LinearLayoutManager(root.context)
                rvAchievement.isNestedScrollingEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val p : Player = (activity as NavBottom).u as Player

        Log.d("desu", "Exp : " + p.exp.toString())
        Log.d("desu", "Streak : " + p.dayStreak.toString())

        val day_streak_count : TextView = root.findViewById(R.id.day_streak_count)
        val exp_count : TextView = root.findViewById(R.id.exp_count)

        day_streak_count.setText(p.dayStreak.toString())
        exp_count.setText(p.exp.toString())

        val tv_follower : TextView = root.findViewById(R.id.follower)
        tv_follower.setText( p.follower.toString().plus(" Follower / ").plus(p.following.toString()).plus(" Following"))


        val btnSetting : ImageButton = root.findViewById(R.id.setting_button)
        btnSetting.setOnClickListener {
            val fragment : Profile = this@User.getParentFragment() as Profile
            fragment.loadFragment(Setting())
        }

        val activity : NavBottom = activity as NavBottom

        val btnAchievement : Button = root.findViewById(R.id.btn_achievements)
        btnAchievement.setOnClickListener {
            (activity as NavBottom?)?.moveToAchievementPage(ach_list)
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