package edu.bluejack20_1.learn_ezo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_1.learn_ezo.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Practice.newInstance] factory method to
 * create an instance of this fragment.
 */
class Practice : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var lesson_list : ArrayList<Lesson>

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
        var root = inflater.inflate(R.layout.fragment_practice, container, false)

        val rvLesson = root.findViewById<View>(R.id.rv_lesson) as RecyclerView
        lesson_list = ArrayList<Lesson>()
        lesson_list.add(Lesson(1, "Basic Letter 1", R.drawable.ic_hiragana, true))
        lesson_list.add(Lesson(2, "Basic Letter 2", R.drawable.ic_katakana, false))
        lesson_list.add(Lesson(3, "Counting", R.drawable.ic_counting, false))
        lesson_list.add(Lesson(4, "Places", R.drawable.ic_place, false))
        lesson_list.add(Lesson(5, "Time", R.drawable.ic_time, false))
        lesson_list.add(Lesson(6, "Basic Vocab", R.drawable.ic_convo, false))

        val rvAdapter = LessonNodeAdapter(lesson_list)
        rvLesson.adapter = rvAdapter
        rvLesson.hasFixedSize()
        rvLesson.layoutManager = LinearLayoutManager(root.context)

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Practice.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Practice().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}