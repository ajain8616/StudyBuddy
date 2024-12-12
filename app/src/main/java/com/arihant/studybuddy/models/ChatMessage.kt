package com.arihant.studybuddy.models

data class ChatMessage(
    val senderId: String = "",
    val senderName: String = "",
    val message: String = "",
    val timestamp: String = "",
    val isRead: Boolean = false,
    val isDelivered: Boolean = false
)
