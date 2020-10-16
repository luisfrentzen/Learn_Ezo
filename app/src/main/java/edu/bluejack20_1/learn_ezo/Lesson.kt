package edu.bluejack20_1.learn_ezo


class Lesson {

    var id : Int ?= null
    var title : String ?= null
    var icon : String ?= null
    var isCompleted : Boolean ?= false

    constructor(id : Int, title : String, icon : String, is_completed : Boolean){
        this.id = id
        this.title = title
        this.icon = icon
        isCompleted = is_completed
    }

    constructor(){

    }

}
