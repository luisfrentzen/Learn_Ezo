package edu.bluejack20_1.learn_ezo

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
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

        val btnSignOut : Button = root.findViewById(R.id.sign_out_button)
        btnSignOut.setOnClickListener {
            Toast.makeText(view?.context, "Button Clicked", Toast.LENGTH_LONG).show()
            (activity as NavBottom?)?.signOut()
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