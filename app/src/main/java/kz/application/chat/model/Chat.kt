package kz.application.chat.model

import com.google.firebase.Timestamp



class Chat(
    var id: String,
    val participantIds: List<String>,
    val participants: List<User>,
    val lastMess: String,
    val lastMessDate: Timestamp
) {
    constructor() : this("", listOf<String>(), listOf<User>(), "", Timestamp.now())
}