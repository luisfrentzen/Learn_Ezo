package edu.bluejack20_1.learn_ezo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
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

    var databaseR : DatabaseReference = FirebaseDatabase.getInstance().getReference("records")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val databaseRecord : DatabaseReference = databaseR.child((activity as NavBottom).u?.id as String)

        var day : Int
        var month : Int
        var year : Int

        val calendarView : CalendarView = root.findViewById(R.id.calendarView) as CalendarView

        databaseRecord.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val events = ArrayList<EventDay>()

                Log.d("db record listener", "yaww");

                var i = 1

                for(data in snapshot.children){
                    val calendar : Calendar = Calendar.getInstance()

                    val list = (data.key.toString()).split("-")

                    day = list[0].toInt()
                    month = list[1].toInt()
                    year = list[2].toInt()

                    calendar.set(year, month, day)

                    if(data.value.toString() == "0"){
                        events.add(EventDay(calendar, R.drawable.ic_dot_red))
                    }else{
                        events.add(EventDay(calendar, R.drawable.ic_dot_green))
                    }

                    i++
                }
                
                calendarView.setEvents(events)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val button_learning_review : Button = root.findViewById<Button>(R.id.review_button)
        val quick_review : Button = root.findViewById<Button>(R.id.quick_review_btn)

        val databaseA : DatabaseReference = FirebaseDatabase.getInstance().getReference("accomplishment").child(
            (activity as NavBottom).u?.id.toString()
        ).child("lessons")

        databaseA.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = snapshot.value.toString()

                val temp_completed = temp.split(",")

                if(temp_completed.size <= 2){
                    button_learning_review.isEnabled = false
                    quick_review.isEnabled = false
                    button_learning_review.alpha = 0.7F
                    quick_review.alpha = 0.7F
                }else{
                    button_learning_review.isEnabled = true
                    quick_review.isEnabled = true
                    button_learning_review.alpha = 1F
                    quick_review.alpha = 1F
                }
            }

        })



        button_learning_review.setOnClickListener {
            (activity as NavBottom).moveToReviewPage(1, (activity as NavBottom).u as Player)
        }



        quick_review.setOnClickListener {
            (activity as NavBottom).moveToReviewPage(2, (activity as NavBottom).u as Player)
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
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }


        fun addUserRecord(id: String, value: Int){

            val cal : Calendar = Calendar.getInstance()

            val day : String = cal.get(Calendar.DATE).toString()
            val month : String = cal.get(Calendar.MONTH).toString()
            val year : String = cal.get(Calendar.YEAR).toString()

            val date : String = day.plus("-").plus(month).plus("-").plus(year)

            var databaseR : DatabaseReference = FirebaseDatabase.getInstance().getReference("records")

            databaseR.child(id).child(date).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var temp = snapshot.value.toString()
                    if(temp == "null"){
                        databaseR.child(id).child(date).setValue(value)
                    }else{
                        if(value != 0){
                            databaseR.child(id).child(date).setValue(value)
                        }
                    }
                }


            })



        }

    }
}