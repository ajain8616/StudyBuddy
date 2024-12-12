package com.arihant.studybuddy.activities

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arihant.studybuddy.R
import com.arihant.studybuddy.adapters.AvatarAdapter
import com.arihant.studybuddy.models.Avatar
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class AvatarActivity : AppCompatActivity() {

    private lateinit var avatarRecyclerView: RecyclerView
    private lateinit var avatarAdapter: AvatarAdapter
    private val avatars = mutableListOf<Avatar>()
    private lateinit var generateButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

        avatarRecyclerView = findViewById(R.id.avatarRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        generateButton = findViewById(R.id.generateButton)

        avatarRecyclerView.layoutManager = LinearLayoutManager(this)
        avatarAdapter = AvatarAdapter(this, avatars)
        avatarRecyclerView.adapter = avatarAdapter

        generateButton.setOnClickListener {
            generateRandomAvatars()
        }

        }

    private fun generateRandomAvatars() {
        val client = OkHttpClient()
        val handler = Handler(Looper.getMainLooper())

        // Show ProgressBar and reset progress
        progressBar.visibility = ProgressBar.VISIBLE
        progressBar.progress = 0

        // Clear existing avatars
        avatars.clear()
        avatarAdapter.notifyDataSetChanged()

        handler.postDelayed({
            for (i in 1..25) {
                val randomName = generateRandomString()
                val randomSet = (1..4).random()
                val url = "https://robohash.org/$randomName?set=set$randomSet"
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

                                    // Update progress as each avatar loads
                                    val progress = (avatars.size * 4) // Progress logic
                                    progressBar.progress = progress.coerceAtMost(100)

                                    // Scroll to the first item if it's the first update
                                    if (avatars.size == 25) {
                                        avatarRecyclerView.scrollToPosition(0)
                                    }
                                }
                            }
                        }
                    }
                })
            }

            // Hide ProgressBar and show RecyclerView after all avatars are loaded
            handler.postDelayed({
                progressBar.visibility = ProgressBar.GONE
                avatarRecyclerView.visibility = RecyclerView.VISIBLE
            }, 22000)
        }, 2000)
    }



    private fun generateRandomString(length: Int = 8): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }
}
