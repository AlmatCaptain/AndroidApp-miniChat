package kz.application.chat.model

data class User(
    var uid: String,
    val username: String,
    val email: String,
    val status: Boolean
) {
    constructor(): this("","","",false)
}