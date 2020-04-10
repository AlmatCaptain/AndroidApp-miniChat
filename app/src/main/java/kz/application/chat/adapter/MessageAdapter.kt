package kz.application.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_item_message_in.view.*
import kz.application.chat.Firebase
import kz.application.chat.R
import kz.application.chat.model.Message
import java.text.SimpleDateFormat

class MessageAdapter(
    private val messages: List<Message> = emptyList()
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutResource = if (viewType == 0)
            R.layout.layout_item_message_out
        else
            R.layout.layout_item_message_in

        return MessageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(layoutResource, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bindMessage(message = messages[position])
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {

        return if (messages[position].senderid == Firebase.auth.currentUser?.uid)
            0
        else
            1
    }

    inner class MessageViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bindMessage(message: Message) {
            val simpleDateFormat = SimpleDateFormat("MMM dd, hh:mm");
            view.text_view.text = message.msg
            view.date_view.text = simpleDateFormat.format(message.time.toDate())
        }
    }
}