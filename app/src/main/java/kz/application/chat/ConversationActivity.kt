package kz.application.chat

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_conversation.*
import kz.application.chat.adapter.MessageAdapter
import kz.application.chat.model.Message


class ConversationActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CHAT = "chat"
        const val EXTRA_USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        viewUI()
    }

    override fun onResume() {
        super.onResume()
        viewUI()
    }

    private fun viewUI() {
        setTitle(intent.getStringExtra(EXTRA_USER))

        recycler_view.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = true
        }

        Firebase.database
            .collection("messages").orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                val query = querySnapshot?.documents
                val messages = mutableListOf<Message>()
                val chatid = intent.getStringExtra(EXTRA_CHAT)

                for (doc in query!!) {
                    val msg = doc.toObject(Message::class.java)

                    if (msg != null && chatid == msg.chatid) {
                        messages.add(msg)
                    }
                }

                recycler_view.adapter = MessageAdapter(messages)
            }

        send_button.setOnClickListener {

            val msg = message_edit_text.text.toString()
            val senderid = Firebase.auth.currentUser?.uid
            val time = Timestamp.now()
            val chatid = intent.getStringExtra(EXTRA_CHAT)

            val message = hashMapOf(
                "chatid" to chatid,
                "senderid" to senderid,
                "msg" to msg,
                "time" to time
            )

            val chat = mapOf(
                "lastMess" to msg,
                "lastMessDate" to time
            )

            Firebase.database.collection("chats").document(chatid).update(chat)

            Firebase.database
                .collection("messages")
                .document()
                .set(message).addOnCompleteListener {
                    if(it.isSuccessful){
                        message_edit_text.text.clear()
                        onResume()
                        return@addOnCompleteListener
                    }
                }
        }
    }


}
