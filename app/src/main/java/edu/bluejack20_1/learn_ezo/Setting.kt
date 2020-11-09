package edu.bluejack20_1.learn_ezo

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.suke.widget.SwitchButton
import kotlinx.android.synthetic.main.fragment_setting.view.*
import java.util.*


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

    lateinit var reminderSwitch : SwitchButton
    lateinit var darkmodeSwitch : SwitchButton

    lateinit var p : Player

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_setting, container, false)

        activity_nav = (activity as NavBottom)

        val btnSignOut : Button = root.findViewById(R.id.sign_out_button)
        btnSignOut.setOnClickListener {
            (activity as NavBottom?)?.signOut()
        }

        val temp : Player = activity_nav?.u as Player

        p = temp

        var tv_reminder : TextView = root.findViewById(R.id.tv_reminder)
        var tv_goal : TextView = root.findViewById(R.id.tv_goal)

        var databaseP : DatabaseReference = databaseU.child(temp.id)

        databaseP.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                p = snapshot.getValue(Player::class.java) as Player

                tv_reminder.setText(p.dailyReminder)

                if(p.practiceGoal == 15){
                    tv_goal.setText(R.string._15_minutes)
                }else if(p.practiceGoal == 30){
                    tv_goal.setText(R.string._30_minutes)
                }else{
                    tv_goal.setText(R.string._45_minutes)
                }

            }

        })

        darkmodeSwitch = root.darkmode_switch

        val reminderTextView : TextView = root.findViewById(R.id.tv_reminder)
        reminderTextView.setOnClickListener {
            val dialog = SetGoalDialog(activity_nav!!, this, "reminder")
            dialog.show()
        }

        reminderSwitch = root.findViewById(R.id.reminder_switch)

        databaseP.child("reminder").addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                reminderSwitch.isChecked = snapshot.value.toString() == "on"
            }

        })

        reminderSwitch.setOnCheckedChangeListener { view, isChecked ->
            if(isChecked){
                onReminder(reminderTextView.text as String, activity_nav!!)

                databaseU.child(activity_nav?.u?.id.toString()).child("reminder").setValue("on")
            }else{
                offReminder(activity_nav)

                databaseU.child(activity_nav?.u?.id.toString()).child("reminder").setValue("off")
            }
        }

        val sharedPreferences: SharedPreferences? = this.activity?.getSharedPreferences(
            "sharedPrefs", MODE_PRIVATE
        )
        val editor = sharedPreferences?.edit()
        val isDarkModeOn = sharedPreferences
            ?.getBoolean(
                "isDarkModeOn", false
            )

        if (isDarkModeOn != null) {
            darkmodeSwitch.isChecked = isDarkModeOn
        }

        darkmodeSwitch.setOnCheckedChangeListener { view, isChecked ->
            if(isChecked){
                    AppCompatDelegate
                        .setDefaultNightMode(
                            AppCompatDelegate
                                .MODE_NIGHT_YES);

                editor!!.putBoolean(
                    "isDarkModeOn", true);
                editor.apply();
            }
            else{
                    AppCompatDelegate
                        .setDefaultNightMode(
                            AppCompatDelegate
                                .MODE_NIGHT_NO);

                editor!!.putBoolean(
                    "isDarkModeOn", false);
                editor.apply();
            }
        }

        val goalTextView : TextView = root.findViewById(R.id.tv_goal)
        goalTextView.setOnClickListener {
            val dialog = SetGoalDialog(activity_nav!!, this, "goal")
            dialog.show()
        }

        val btnBack : ImageButton = root.findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            val fragment : Profile = this@Setting.getParentFragment() as Profile

            fragment.loadFragment(User())
        }



        return root

    }

    fun onReminder(g : String, ctx: Context){
        var c = Calendar.getInstance()
        var rNow = Calendar.getInstance()

        var tempHour = g.split(":")

        var tempMin = tempHour[1].split(" ")

        var hour = tempHour[0]
        var minute = tempMin[0]

        if(tempMin[1] == "AM"){
            c.set(Calendar.AM_PM, Calendar.AM)
        }else{
            c.set(Calendar.AM_PM, Calendar.PM)
        }

        Log.d("hehe", hour.plus(minute))

        c.set(Calendar.HOUR, hour.toInt())
        c.set(Calendar.MINUTE, minute.toInt())
        c.set(Calendar.SECOND, 0)

        var intent = Intent(ctx, AlertReceiver::class.java)

        var pendingIntent :PendingIntent = PendingIntent.getBroadcast(ctx, 1, intent, 0)

        var alarmManager : AlarmManager = ctx.getSystemService(ALARM_SERVICE) as AlarmManager

        offReminder(ctx)

        if(c.timeInMillis > rNow.timeInMillis){
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
        }

    }

    fun offReminder(ctx: Context?){
        var intent = Intent(ctx, AlertReceiver::class.java)

        var pendingIntent :PendingIntent = PendingIntent.getBroadcast(ctx, 1, intent, 0)

        var alarmManager : AlarmManager = ctx?.getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
    }

    fun setGoal(g : String){

        var temp = g.split(" ")

        databaseU.child(activity_nav?.u?.id.toString()).child("practiceGoal").setValue(temp[0].toInt())
    }

    fun setReminder(g : String){

        databaseU.child(activity_nav?.u?.id.toString()).child("dailyReminder").setValue(g)

        if(p.reminder == "on"){
            offReminder((activity as NavBottom))
            onReminder(g, (activity as NavBottom))
        }
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