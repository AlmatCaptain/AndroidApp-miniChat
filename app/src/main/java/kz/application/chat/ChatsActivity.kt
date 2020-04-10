package kz.application.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_chats.*
import kotlinx.android.synthetic.main.activity_chats.recycler_view
import kz.application.chat.adapter.ChatAdapter
import kz.application.chat.model.Chat
import kz.application.chat.model.User


class ChatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)


        viewUI()
    }

    override fun onResume() {
        super.onResume()
        viewUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logout_button -> {
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    fun viewUI() {

        if(Firebase.auth.currentUser != null)
            Log.d("ffff",Firebase.auth.currentUser?.email+" chats")

        start_conversation.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UsersActivity::class.java
                )
            )
        }

        val documents = Firebase.database.collection("chats")
            .orderBy("lastMessDate",Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val query = querySnapshot?.documents
                val chats = mutableListOf<Chat>()

                for (doc in query!!) {
                    val chat = doc.toObject(Chat::class.java)

                    if (chat != null) {
                        if(chat.participantIds.contains(Firebase.auth.currentUser?.uid))
                            chats.add(chat)
                    }

                }
                recycler_view.layoutManager = LinearLayoutManager(this)
                recycler_view.adapter =
                    ChatAdapter(chats.toList(), { chat: Chat, user: User -> itemClicked(chat,user) })
            }
    }

    private fun itemClicked(chat: Chat,user: User) {
        val intent = Intent(this, ConversationActivity::class.java)
        intent.putExtra(ConversationActivity.EXTRA_CHAT, chat.id)
        intent.putExtra(ConversationActivity.EXTRA_USER, user.email)
        startActivity(intent)
    }
}
