package com.example.sero_service_admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sero_service_admin.R
import com.example.sero_service_admin.databinding.ItemUserBinding
import com.example.sero_service_admin.model.User

class UserAdapter(
    var click: ClickListener
) :
    ListAdapter<User, UserAdapter.UserViewHolder>(DiffCallBack()) {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(user: User, position: Int) {
            val bind = ItemUserBinding.bind(itemView)


            bind.apply {
                tvUsername.text = user.name
                tvPassword.text = user.password
                tvLogin.text = user.login
                btnRemove.setOnClickListener {
                    click.onRemoveClick(user)
                }
                btnChange.setOnClickListener {
                    click.onEditClick(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    interface ClickListener {
        fun onRemoveClick(user: User)
        fun onEditClick(user: User)
    }

    class DiffCallBack : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
}