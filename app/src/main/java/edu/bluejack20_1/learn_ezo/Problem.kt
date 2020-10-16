package edu.bluejack20_1.learn_ezo

import java.io.Serializable

class Problem constructor(problem : String, ans : String, choices : ArrayList<String>) : Serializable{

    val problem = problem
    val ans = ans
    val choices = choices
}