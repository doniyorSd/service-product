package com.example.sero_service_admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sero_service_admin.R
import com.example.sero_service_admin.databinding.ItemFromBinding
import com.example.sero_service_admin.databinding.ItemToBinding
import com.example.sero_service_admin.model.Message

class MessageAdapter(private val listMessage: ArrayList<Message>, var uid: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        if (listMessage[position].fromUser == uid) {
            return 1
        } else {
            return 2
        }
    }

    inner class FromViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(message: Message) {
            val bind = ItemFromBinding.bind(itemView)
            bind.tvMessage.text = message.message
            bind.tvTime.text = message.dateAndTime

            if (message.userName != null)
                bind.tvName.text = message.userName
        }
    }

    inner class ToViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(message: Message) {
            val bind = ItemToBinding.bind(itemView)
            bind.tvMessage.text = message.message
            bind.tvTime.text = message.dateAndTime
            if (message.userName != null)
                bind.tvName.text = message.userName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_from, parent, false)
            return FromViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_to, parent, false)
            return ToViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            (holder as FromViewHolder).onBind(listMessage[position])
        } else {
            (holder as ToViewHolder).onBind(listMessage[position])
        }
    }

    override fun getItemCount(): Int = listMessage.size
}