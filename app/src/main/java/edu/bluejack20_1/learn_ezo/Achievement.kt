package edu.bluejack20_1.learn_ezo

import java.io.Serializable

class Achievement constructor(
    id : Int,
    title : String,
    desc : String,
    currentProgress : Int,
    targetProgress : Int,
    icon : Int,
    isCompleteed : Boolean) : Serializable{

    var achievement_id = id
    var achievement_title = title
    var achievement_desc = desc
    var achievement_current_progress = currentProgress
    var achievement_target_proggress = targetProgress
    var achievement_is_completed = isCompleteed
    var achievement_icon = icon

}