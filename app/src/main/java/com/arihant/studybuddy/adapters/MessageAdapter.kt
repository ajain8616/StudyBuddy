package com.arihant.studybuddy.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arihant.studybuddy.R
import com.arihant.studybuddy.activities.ChattingActivity
import com.arihant.studybuddy.models.Message
import com.bumptech.glide.Glide

class MessageAdapter(private val messageList: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewProfile: ImageView = itemView.findViewById(R.id.imageViewProfile)
        val textViewSenderName: TextView = itemView.findViewById(R.id.textViewSenderName)
        val textViewMessage: TextView = itemView.findViewById(R.id.textViewMessage)
        val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
        val messageCount: TextView = itemView.findViewById(R.id.messageCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.persons_chat, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]
        holder.textViewSenderName.text = currentMessage.senderName
        holder.textViewMessage.text = currentMessage.message
        holder.textViewTime.text = currentMessage.time
        holder.messageCount.text = currentMessage.messageCount.toString()

        // Load profile image using Glide
        Glide.with(holder.itemView.context)
            .load(currentMessage.profileImageUrl)
            .placeholder(R.drawable.ic_profile_photo)
            .into(holder.imageViewProfile)

        // Set an OnClickListener for the entire item
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ChattingActivity::class.java).apply {
                putExtra("senderName", currentMessage.senderName)
                putExtra("profileImageUrl", currentMessage.profileImageUrl)
                putExtra("senderUid", currentMessage.senderUid)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}
