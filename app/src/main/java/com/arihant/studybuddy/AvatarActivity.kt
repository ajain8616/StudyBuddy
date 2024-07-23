package com.arihant.studybuddy

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class AvatarActivity : AppCompatActivity() {

    private lateinit var avatarRecyclerView: RecyclerView
    private lateinit var avatarAdapter: AvatarAdapter
    private val avatars = mutableListOf<Avatar>()
    private lateinit var generateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

        avatarRecyclerView = findViewById(R.id.avatarRecyclerView)
        avatarRecyclerView.layoutManager = LinearLayoutManager(this)

        avatarAdapter = AvatarAdapter(this, avatars) // Pass the context here
        avatarRecyclerView.adapter = avatarAdapter

        generateButton = findViewById(R.id.generateButton)
        generateButton.setOnClickListener {
            generateRandomAvatars()
        }

        // Initial load
        generateRandomAvatars()
    }

    private fun generateRandomAvatars() {
        val client = OkHttpClient()
        val handler = Handler(Looper.getMainLooper())

        // Clear existing avatars
        avatars.clear()
        avatarAdapter.notifyDataSetChanged()

        for (i in 1..20) {
            val randomName = generateRandomString()
            val url = "https://robohash.org/$randomName.png"

            val request = Request.Builder().url(url).build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.isSuccessful) {
                        response.body?.let { body ->
                            val bitmap = BitmapFactory.decodeStream(body.byteStream())
                            handler.post {
                                avatars.add(Avatar(randomName, bitmap))
                                avatarAdapter.notifyItemInserted(avatars.size - 1)

                                // Scroll to the first item if it's the first update
                                if (avatars.size == 20) {
                                    avatarRecyclerView.scrollToPosition(0)
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    private fun generateRandomString(length: Int = 8): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }
}
