package com.arihant.studybuddy

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountActivity : AppCompatActivity() {

    private lateinit var tvAccountSettings: TextView
    private lateinit var mainLinearLayout: LinearLayout
    private lateinit var twoStepVerificationCard: CardView
    private lateinit var deleteAccountCard: CardView
    private lateinit var additionalAccountSettingsCard: CardView
    private lateinit var profileSettingsLinearLayout: LinearLayout
    private lateinit var layoutProfileAvatar: CardView
    private lateinit var layoutProfilePrivacy: CardView
    private lateinit var layoutProfileNotification: CardView
    private lateinit var layoutProfileChat: CardView
    private lateinit var layoutProfileScheduleReminder: CardView
    private lateinit var layoutProfileAppLanguage: CardView
    private lateinit var switchTwoStepVerification: Switch

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        initialize()
    }

    private fun initialize() {
        findViews()
        setUpOnClickListeners()
        checkTwoStepVerificationStatus()
    }

    private fun findViews() {
        tvAccountSettings = findViewById(R.id.tvAccountSettings)
        mainLinearLayout = findViewById(R.id.mainLinearLayout)
        twoStepVerificationCard = findViewById(R.id.twoStepVerificationCard)
        deleteAccountCard = findViewById(R.id.deleteAccountCard)
        additionalAccountSettingsCard = findViewById(R.id.additionalAccountSettingsCard)
        profileSettingsLinearLayout = findViewById(R.id.profileSettingsLinearLayout)
        layoutProfileAvatar = findViewById(R.id.layoutProfileAvatar)
        layoutProfilePrivacy = findViewById(R.id.layoutProfilePrivacy)
        layoutProfileNotification = findViewById(R.id.layoutProfileNotification)
        layoutProfileChat = findViewById(R.id.layoutProfileChat)
        layoutProfileScheduleReminder = findViewById(R.id.layoutProfileScheduleReminder)
        layoutProfileAppLanguage = findViewById(R.id.layoutProfileAppLanguage)
        switchTwoStepVerification = findViewById(R.id.switchTwoStepVerification)
    }

    private fun setUpOnClickListeners() {
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

        twoStepVerificationCard.setOnClickListener {
            if (switchTwoStepVerification.visibility == View.GONE) {
                switchTwoStepVerification.visibility = View.VISIBLE
            } else {
                switchTwoStepVerification.visibility = View.GONE
            }
        }

        additionalAccountSettingsCard.setOnClickListener {
            if (profileSettingsLinearLayout.visibility == View.GONE) {
                profileSettingsLinearLayout.visibility = View.VISIBLE
                mainLinearLayout.visibility = View.GONE
            } else {
                profileSettingsLinearLayout.visibility = View.GONE
                mainLinearLayout.visibility = View.VISIBLE
            }
        }

        switchTwoStepVerification.setOnCheckedChangeListener { _, isChecked ->
            updateTwoStepVerificationStatus(isChecked)
        }

        // Handle delete account card click
        deleteAccountCard.setOnClickListener {
            showDeleteAccountConfirmationDialog()
        }
    }

    private fun showDeleteAccountConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Account")
        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.")
        builder.setPositiveButton("Yes") { _, _ ->
            deleteAccount()
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }

    private fun deleteAccount() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                auth.signOut()
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateTwoStepVerificationStatus(isEnabled: Boolean) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .update("twoStepVerificationEnabled", isEnabled)
            .addOnSuccessListener {
                Toast.makeText(this, "Two-Step Verification status updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update Two-Step Verification status", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkTwoStepVerificationStatus() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val isEnabled = document.getBoolean("twoStepVerificationEnabled") ?: false
                    switchTwoStepVerification.isChecked = isEnabled
                    switchTwoStepVerification.visibility = if (isEnabled) View.VISIBLE else View.GONE
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to retrieve Two-Step Verification status", Toast.LENGTH_SHORT).show()
            }
    }
}
