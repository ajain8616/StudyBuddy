package com.arihant.studybuddy.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.arihant.studybuddy.adapters.ChattingAdapter
import com.arihant.studybuddy.R
import com.arihant.studybuddy.models.ChatMessage
import com.bumptech.glide.Glide

class ChattingActivity : AppCompatActivity() {

    // Variables for views
    private lateinit var textViewSenderName: TextView
    private lateinit var imageViewProfile: ImageView
    private lateinit var recyclerViewChat: RecyclerView
    private lateinit var inputMessage: EditText
    private lateinit var sendButton: ImageView

    private val chatMessages = mutableListOf<ChatMessage>()
    private lateinit var chattingAdapter: ChattingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        // Initialize views
        textViewSenderName = findViewById(R.id.senderNameText)
        imageViewProfile = findViewById(R.id.profileImage)
        recyclerViewChat = findViewById(R.id.recyclerViewChat)
        inputMessage = findViewById(R.id.inputMessage)
        sendButton = findViewById(R.id.sendButton)

        // Retrieve senderName and profileImageUrl from the intent
        val senderUid = intent.getStringExtra("senderUid") ?: ""
        val senderName = intent.getStringExtra("senderName") ?: "Unknown"
        val profileImageUrl = intent.getStringExtra("profileImageUrl")
        textViewSenderName.text = senderName
        profileImageUrl?.let {
            Glide.with(this).load(it).into(imageViewProfile)
        }


    }
}
