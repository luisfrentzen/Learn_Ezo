package edu.bluejack20_1.learn_ezo

import java.io.Serializable


class Lesson : Serializable{

    var id : Int ?= null
    var title : String ?= null
    var icon : String ?= null
    var short_lesson : String? = null
    var isCompleted : Boolean ?= false

    constructor(id : Int, title : String, icon : String, is_completed : Boolean, short_lesson : String){
        this.id = id
        this.title = title
        this.icon = icon
        this.short_lesson = short_lesson
        isCompleted = is_completed
    }

    constructor(){

    }

}
