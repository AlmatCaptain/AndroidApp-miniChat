package kz.application.chat.model

import com.google.firebase.Timestamp

data class Message(
    val chatid: String,
    val senderid : String,
    val msg : String,
    val time : Timestamp
){
    constructor(): this("","","",Timestamp.now())
}