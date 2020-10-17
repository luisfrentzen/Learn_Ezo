package edu.bluejack20_1.learn_ezo

import android.util.Log
import java.io.Serializable

class Problem constructor(problem : String, ans : String, choices : ArrayList<String>) : Serializable{

    val problem = problem
    val ans = ans
    val choices = choices

    companion object {
        fun createProblems(range: Int, alf : ArrayList<String>, jpn : ArrayList<String>, rom : ArrayList<String>) : ArrayList<Problem>{
            //tipe soal 1 = alf - jpn
            //tipe soal 2 = jpn - alf
            //tipe soal 3 = jpn - rom

            val arProb = ArrayList<Problem>()

            for( i in 0 until range ){
                val type : Int = (1..3).random()

                Log.d("type", type.toString())

                val maxSize : Int = alf.size
                val idx : Int = (0..maxSize-1).random()

                lateinit var prob : String
                lateinit var ans : String
                lateinit var choices : ArrayList<String>

                lateinit var temp : ArrayList<Problem>

                when(type){
                    1 -> {
                        prob = alf.get(idx)
                        ans = jpn.get(idx)

                        temp = jpn.toMutableList() as ArrayList<Problem>
                        temp.removeAt(idx)
                        temp.shuffle()

                    }
                    2 -> {
                        prob = jpn.get(idx)
                        ans = alf.get(idx)

                        temp = alf.toMutableList() as ArrayList<Problem>
                        temp.removeAt(idx)
                        temp.shuffle()

                    }
                    3 -> {
                        prob = jpn.get(idx)
                        ans = rom.get(idx)

                        temp = rom.toMutableList() as ArrayList<Problem>
                        temp.removeAt(idx)
                        temp.shuffle()

                    }
                }

                choices = temp.take(3) as ArrayList<String>

                choices.add(ans)
                choices.shuffle()

                arProb.add(Problem(prob, ans, choices))

            }

            return arProb
        }
    }
}
