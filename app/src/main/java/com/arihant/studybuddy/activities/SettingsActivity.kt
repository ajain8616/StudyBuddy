package com.arihant.studybuddy.activities

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.arihant.studybuddy.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var layoutProfileAccount: RelativeLayout
    private lateinit var layoutProfileAvatar: RelativeLayout
    private lateinit var layoutProfilePrivacy: RelativeLayout
    private lateinit var layoutProfileNotification: RelativeLayout
    private lateinit var layoutProfileChat: RelativeLayout
    private lateinit var layoutProfileScheduleReminder: RelativeLayout
    private lateinit var layoutProfileAppLanguage: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize all views
        layoutProfileAccount = findViewById(R.id.layoutProfileAccount)
        layoutProfileAvatar = findViewById(R.id.layoutProfileAvatar)
        layoutProfilePrivacy = findViewById(R.id.layoutProfilePrivacy)
        layoutProfileNotification = findViewById(R.id.layoutProfileNotification)
        layoutProfileChat = findViewById(R.id.layoutProfileChat)
        layoutProfileScheduleReminder = findViewById(R.id.layoutProfileScheduleReminder)
        layoutProfileAppLanguage = findViewById(R.id.layoutProfileAppLanguage)

        // Set click listeners
        layoutProfileAccount.setOnClickListener {
            startActivity(Intent(this, AccountActivity::class.java))
        }

        layoutProfileAvatar.setOnClickListener {
            startActivity(Intent(this, AvatarActivity::class.java))
        }

        layoutProfilePrivacy.setOnClickListener {
            startActivity(Intent(this, PrivacyActivity::class.java))
        }

        layoutProfileNotification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        layoutProfileChat.setOnClickListener {
            startActivity(Intent(this, ChatSettingsActivity::class.java))
        }

        layoutProfileScheduleReminder.setOnClickListener {
            startActivity(Intent(this, ReminderActivity::class.java))
        }

        layoutProfileAppLanguage.setOnClickListener {
            startActivity(Intent(this, LanguageActivity::class.java))
        }
    }
}
