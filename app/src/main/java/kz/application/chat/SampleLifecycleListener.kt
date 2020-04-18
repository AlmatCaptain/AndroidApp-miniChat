package kz.application.chat

import android.content.Intent
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kz.application.chat.model.User

class SampleLifecycleListener : LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        Log.d("SampleLifecycle", "Returning to foreground…")
        Firebase.database.collection("users").get().addOnSuccessListener { u ->

            val users = mutableListOf<User>()

            for (doc in u) {
                val user = doc.toObject(User::class.java)

                if (Firebase.auth.currentUser?.uid == user.uid) {
                    val userHash = hashMapOf(
                        "email" to user.email,
                        "username" to user.username,
                        "uid" to user.uid,
                        "status" to true
                    )

                    Firebase.database
                        .collection("users")
                        .document(user.uid).set(userHash)
                }
            }
            return@addOnSuccessListener
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        Log.d("SampleLifecycle", "Returning to foreground…bbb")
        Firebase.database.collection("users").get().addOnSuccessListener { u ->

            val users = mutableListOf<User>()

            for (doc in u) {
                val user = doc.toObject(User::class.java)

                if (Firebase.auth.currentUser?.uid == user.uid) {
                    val userHash = hashMapOf(
                        "email" to user.email,
                        "username" to user.username,
                        "uid" to user.uid,
                        "status" to false
                    )

                    Firebase.database
                        .collection("users")
                        .document(user.uid).set(userHash)
                }
            }
            return@addOnSuccessListener
        }
    }

}
