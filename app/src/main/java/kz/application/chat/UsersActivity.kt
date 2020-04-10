package kz.application.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.activity_users.*
import kz.application.chat.adapter.UserAdapter
import kz.application.chat.model.Chat
import kz.application.chat.model.User
import java.text.SimpleDateFormat
import kotlin.random.Random

class UsersActivity : AppCompatActivity() {

    private var participantUsers = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        viewUI()
    }

    private fun viewUI() {

        Firebase.database.collection("users").get().addOnSuccessListener { u ->
            Firebase.database.collection("chats").get().addOnSuccessListener { ch ->
                val users = mutableListOf<User>()
                val chats = mutableListOf<Chat>()



                for (doc in ch) {
                    val chat = doc.toObject(Chat::class.java)

                    Log.d("ggLoad", chats.toString())
                    chats.add(chat)
                }

                for (doc in u) {
                    val user = doc.toObject(User::class.java)

                    if (Firebase.auth.currentUser?.uid == user.uid)
                        participantUsers.add(user)
                    else {
                        if (chats.find {
                                it.participantIds.containsAll(
                                    listOf(
                                        Firebase.auth.currentUser?.uid,
                                        user.uid
                                    )
                                )
                            } == null) {
                            users.add(user)
                        }
                    }

                }

                Log.d("ggLoad", users.toString() + " users")

                recycler_view.layoutManager = LinearLayoutManager(this)
                recycler_view.adapter =
                    UserAdapter(users.toList(), { user: User -> itemClicked(user) })
            }

        }
    }

    private fun itemClicked(user: User) {
        participantUsers.add(user)

        val chatid = Firebase.database
            .collection("chats")
            .document().id

        val chat = hashMapOf(
            "id" to chatid,
            "participantIds" to listOf<String>(user.uid, Firebase.auth.currentUser!!.uid),
            "participants" to participantUsers,
            "lastMess" to "",
            "lastMessDate" to Timestamp.now()
        )


        Firebase.database
            .collection("chats")
            .document(chatid).set(chat).addOnSuccessListener {
                val intent = Intent(this, ConversationActivity::class.java)
                intent.putExtra(ConversationActivity.EXTRA_CHAT, chatid)
                intent.putExtra(ConversationActivity.EXTRA_USER, user.email)
                startActivity(intent)
                participantUsers.clear()
                finish()
                return@addOnSuccessListener
            }

    }

}

