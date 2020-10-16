package edu.bluejack20_1.learn_ezo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PracticePreferencesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PracticePreferencesFragment : Fragment() {
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
        val root = inflater.inflate(R.layout.fragment_practice_preferences, container, false)

        var fifteenMin = root.findViewById<RadioButton>(R.id.five_min)
        var thirtyMin = root.findViewById<RadioButton>(R.id.ten_min)
        var fortyFiveMin = root.findViewById<RadioButton>(R.id.fifteen_min)

        var setup_activity = (activity as SetupActivity)

        val nextBtn = root.findViewById<Button>(R.id.next_button_2)
        nextBtn.setOnClickListener{

            if(fifteenMin.isChecked){
                setup_activity.u?.practiceGoal = 15
            }else if(thirtyMin.isChecked){
                setup_activity.u?.practiceGoal = 30
            }else if(fortyFiveMin.isChecked){
                setup_activity.u?.practiceGoal = 45
            }

            Log.d("desu", setup_activity.u?.practiceGoal.toString())

            setup_activity!!.loadFragment(ReminderPreferencesFragment())
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
         * @return A new instance of fragment PracticePreferencesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PracticePreferencesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}