package edu.bluejack20_1.learn_ezo

import android.graphics.Typeface
import android.icu.lang.UProperty.INT_START
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_notification.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationFragment : Fragment() {
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

    val arAct = ArrayList<SpannableStringBuilder>()
    val arTime = ArrayList<Calendar>()

    lateinit var rvAdapter : NotificationCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_notification, container, false)

        rvAdapter = NotificationCardAdapter(arAct, arTime)
        root.rvNotif.adapter = rvAdapter
        root.rvNotif.hasFixedSize()
        root.rvNotif.layoutManager = LinearLayoutManager(root.context)

        return root
    }

    override fun onResume() {

        val cal = Calendar.getInstance()

        val databaseNotif = FirebaseDatabase.getInstance().getReference("notifications")
        databaseNotif.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                arAct.clear()
                arTime.clear()

                val arFollowed = ArrayList<String>()

                val databaseProfile = FirebaseDatabase.getInstance().getReference("users").child((activity as NavBottom).u?.id.toString()).child("following-list")

                databaseProfile.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(sn: DataSnapshot) {
                        for(d in sn.children){
                            arFollowed.add(d.key.toString())
                        }

                        for(data in snapshot.children){
                            if(!arFollowed.contains(data.child("userid").value.toString())){
                                continue;
                            }

                            val date = data.child("timestamp").value.toString()
                            val cal2 = Calendar.getInstance()
                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            val date2 = sdf.parse(date) as Date
                            cal2.time = date2
                            arTime.add(cal2)

                            val databaseUser = FirebaseDatabase.getInstance().getReference("users").child(data.child("userid").value.toString())
                            databaseUser.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onCancelled(error: DatabaseError) {

                                }

                                override fun onDataChange(snapshot: DataSnapshot) {

                                    var str : SpannableStringBuilder = SpannableStringBuilder("")

                                    if(data.child("type").value.toString().equals("lesson")){
                                        str = SpannableStringBuilder(snapshot.child("name").value.toString() + " " + resources.getString(R.string.has_completed_lesson) + " " + data.child("lesson").value)
                                    }
                                    else if(data.child("type").value.toString().equals("follow")){
                                        str = SpannableStringBuilder(snapshot.child("name").value.toString() + " " + resources.getString(R.string.followed_you))
                                    }

                                    str.setSpan(
                                        StyleSpan(Typeface.BOLD),
                                        0,
                                        snapshot.child("name").value.toString().length,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )

                                    arAct.add(str)

                                    rvAdapter.notifyDataSetChanged()
                                }

                            })
                        }
                    }

                })



            }

        })
        super.onResume()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotificationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}