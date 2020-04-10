package kz.application.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_item.view.*
import kz.application.chat.R
import kz.application.chat.model.User

class UserAdapter(
    private val listItems: List<User> = listOf(),
    private val clickListener: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(user: User, clickListener: (User) -> Unit) {
            view.username_view.text = user.username
            view.email_view.text = user.email

            view.setOnClickListener { clickListener(user) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        )

    override fun getItemCount() = listItems.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItem(listItems[position], clickListener)
    }

}

