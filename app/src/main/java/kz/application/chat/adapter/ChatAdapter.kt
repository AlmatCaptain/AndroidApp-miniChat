package kz.application.chat.adapter

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

        fun bindItem(chat: Chat, clickListener: (Chat,User) -> Unit) {
            val client = chat.participants.find { u -> u.uid != Firebase.auth.currentUser?.uid }
            val simpleDateFormat = SimpleDateFormat("MMM dd, hh:mm");

            if(client != null){
                view.client_username.text = client.username
                view.last_message.text = chat.lastMess
                view.date_view.text = simpleDateFormat.format(chat.lastMessDate.toDate())
            }

            view.setOnClickListener { clickListener(chat,client!!) }
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
