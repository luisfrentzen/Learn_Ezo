package edu.bluejack20_1.learn_ezo

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Setting.newInstance] factory method to
 * create an instance of this fragment.
 */
class Setting : Fragment() {
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

    var databaseU : DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    var activity_nav: NavBottom ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_setting, container, false)

        activity_nav = (activity as NavBottom)

        val btnSignOut : Button = root.findViewById(R.id.sign_out_button)
        btnSignOut.setOnClickListener {
            Toast.makeText(view?.context, "Button Clicked", Toast.LENGTH_LONG).show()
            (activity as NavBottom?)?.signOut()
        }

        val p : Player = activity_nav?.u as Player

        var tv_reminder : TextView = root.findViewById(R.id.tv_reminder)
        var tv_goal : TextView = root.findViewById(R.id.tv_goal)


        tv_reminder.setText(p.dailyReminder)

        if(p.practiceGoal == 15){
            tv_goal.setText(R.string._15_minutes)
        }else if(p.practiceGoal == 30){
            tv_goal.setText(R.string._30_minutes)
        }else{
            tv_goal.setText(R.string._45_minutes)
        }



        val goalTextView : TextView = root.findViewById(R.id.tv_goal)
        goalTextView.setOnClickListener {
            val dialog = SetGoalDialog(activity_nav!!, this, "goal")
            dialog.show()
        }

        val reminderTextView : TextView = root.findViewById(R.id.tv_reminder)
        reminderTextView.setOnClickListener {
            val dialog = SetGoalDialog(activity_nav!!, this, "reminder")
            dialog.show()
        }

        val btnBack : ImageButton = root.findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            val fragment : Profile = this@Setting.getParentFragment() as Profile

            fragment.loadFragment(User())
        }

        return root
    }

    fun setGoal(g : String){
        val goalTextView = view?.findViewById<TextView>(R.id.tv_goal)


        var temp = g.split(" ")

        if(temp[0] == "15"){
            goalTextView?.setText(R.string._15_minutes)
        }else if(temp[0] == "30"){
            goalTextView?.setText(R.string._30_minutes)
        }else{
            goalTextView?.setText(R.string._45_minutes)
        }
        activity_nav?.u?.practiceGoal = temp[0].toInt()

        databaseU.child(activity_nav?.u?.id.toString()).setValue(activity_nav?.u as Player)
    }

    fun setReminder(g : String){
        val reminderTextView = view?.findViewById<TextView>(R.id.tv_reminder)
        reminderTextView?.setText(g)
        activity_nav?.u?.dailyReminder = g

        databaseU.child(activity_nav?.u?.id.toString()).setValue(activity_nav?.u as Player)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Setting.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Setting().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}