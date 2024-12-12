package com.arihant.studybuddy.models

data class Message(
    val senderUid: String,
    val senderName: String,
    val message: String,
    val time: String,
    val profileImageUrl: String,
    var messageCount: Int = 0
)



