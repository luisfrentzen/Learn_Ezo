package edu.bluejack20_1.learn_ezo

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import org.w3c.dom.Text


class SetGoalDialog (var c: Activity, var f: Setting, var type : String) : Dialog(c), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        when(type) {
            "goal" -> {
                setContentView(R.layout.set_goal_dialog)
                val btnfive = findViewById<Button>(R.id.fivemin_btn)
                btnfive.setOnClickListener {
                    setGoal(btnfive.text.toString())
                    dismiss()
                }

                val btnten = findViewById<Button>(R.id.tenmin_btn)
                btnten.setOnClickListener {
                    setGoal(btnten.text.toString())
                    dismiss()
                }

                val btnfifteen = findViewById<Button>(R.id.fifteenmin_btn)
                btnfifteen.setOnClickListener {
                    setGoal(btnfifteen.text.toString())
                    dismiss()
                }
            }
            "reminder" -> {
                setContentView(R.layout.set_reminder_dialog)
                val reminderTimePicker = findViewById<TimePicker>(R.id.tp_reminder)

                val btnconf = findViewById<Button>(R.id.ok_btn)
                btnconf.setOnClickListener {
                    var hour = reminderTimePicker.hour
                    val minute = reminderTimePicker.minute
                    val form = if ( hour > 11 ) {
                        if ( hour > 12 ){
                            hour = hour - 12
                        }
                        "PM"
                    }
                    else {
                        "AM"
                    }

                    val reminderTime = String.format("%02d", hour) + ":" + String.format("%02d", minute) + " " + form
                    setReminder(reminderTime)
                    dismiss()
                }
            }
        }

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun setGoal(goal : String){
        f.setGoal(goal)
    }

    fun setReminder(reminder : String){
        f.setReminder(reminder)
    }

    override fun onClick(v: View) {
        dismiss()
    }

}