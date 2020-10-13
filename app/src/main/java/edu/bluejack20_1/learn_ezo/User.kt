package edu.bluejack20_1.learn_ezo

import android.net.Uri
import android.os.Bundle
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

        val rvAchievement = root.findViewById<View>(R.id.rv_achievement) as RecyclerView
        var ach_list = ArrayList<Achievement>()
        ach_list.add(Achievement(1, "Hardworker", "Reach 10 days streak", 0, 10, R.drawable.ic_trophy, false))
        ach_list.add(Achievement(2, "Intermediate Student", "Reach level 5", 0, 5, R.drawable.ic_trophy, false))
        ach_list.add(Achievement(3, "Conqueror", "Complete all lessons", 0, 6, R.drawable.ic_trophy, false))
        ach_list.add(Achievement(4, "Social Person", "Follow 3 friends", 0, 3, R.drawable.ic_trophy, false))
        ach_list.add(Achievement(5, "Big Brain", "Complete 3 reviews", 0, 3, R.drawable.ic_trophy, false))
        ach_list.add(Achievement(6, "Popular One", "Have 3 followers", 0, 3, R.drawable.ic_trophy, false))
        ach_list.add(Achievement(7, "Genius", "Complete a lesson with no mistake", 0, 1, R.drawable.ic_trophy, false))

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

        val acct = GoogleSignIn.getLastSignedInAccount(activity)
        if (acct != null) {
            personName = acct.givenName
            personEmail = acct.email
            personId = acct.id
            personPhoto = acct.photoUrl

            Glide.with(this).load(personPhoto).into(profilePic)

            val tv_dname : TextView = root.findViewById(R.id.display_name)
            tv_dname.setText(personName)

            val tv_follower : TextView = root.findViewById(R.id.follower)
            tv_follower.setText("0 Follower / 0 Following")
        }

        var ach_overview = ArrayList<Achievement>(ach_list.subList(0, 3))

        val rvAdapter = AchievementCardAdapter(ach_overview)
        rvAchievement.adapter = rvAdapter
        rvAchievement.hasFixedSize()
        rvAchievement.layoutManager = LinearLayoutManager(root.context)
        rvAchievement.isNestedScrollingEnabled = false

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