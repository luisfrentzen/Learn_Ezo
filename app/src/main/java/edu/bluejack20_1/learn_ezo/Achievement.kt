package edu.bluejack20_1.learn_ezo

import java.io.Serializable


class Achievement : Serializable{

    var id: Int ?= null
    var title: String ?= null
    var desc: String ?= null
    var currentProgress: Int ?= null
    var goal: Int ?= null
    var icon: String ?= null

    constructor(
        id : Int,
        title : String,
        desc : String,
        currentProgress: Int,
        targetProgress : Int,
        icon : String){

        this.id = id
        this.title = title
        this.desc = desc
        this.currentProgress = currentProgress
        this.goal = targetProgress
        this.icon = icon

    }

    constructor(){

    }

}
