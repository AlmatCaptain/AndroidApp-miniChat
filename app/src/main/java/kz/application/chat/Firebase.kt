package kz.application.chat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kz.application.chat.model.Chat
import kz.application.chat.model.User

class Firebase {
    companion object {
        val auth by lazy { FirebaseAuth.getInstance() }
        val database by lazy { FirebaseFirestore.getInstance() }
    }
}
