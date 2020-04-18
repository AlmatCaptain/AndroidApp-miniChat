package kz.application.chat.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chat_item.view.*
import kz.application.chat.Firebase
import kz.application.chat.R
import kz.application.chat.model.Chat
import kz.application.chat.model.User
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ChatAdapter(
    private val listItems: List<Chat> = listOf(),
    private val clickListener: (Chat, User) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bindItem(chat: Chat, clickListener: (Chat, User) -> Unit) {
            val client = chat.participantIds.find { u -> u != Firebase.auth.currentUser?.uid }
            val simpleDateFormat = SimpleDateFormat("MMM dd, hh:mm");
            Firebase.database.collection("users").get().addOnSuccessListener { u ->

                for (doc in u) {
                    val user = doc.toObject(User::class.java)

                    if (client == user.uid) {
                        view.client_username.text = user.username
                        view.last_message.text = chat.lastMess
                        view.date_view.text = simpleDateFormat.format(chat.lastMessDate.toDate())

                        if (!user.status) {
                            view.status_user.text = "offline"
                        } else
                            view.status_user.text = "online"
                    }
                    view.setOnClickListener { clickListener(chat, user!!) }
                }




            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        )

    override fun getItemCount() = listItems.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItem(listItems[position], clickListener)
    }

}
