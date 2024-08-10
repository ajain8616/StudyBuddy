package com.arihant.studybuddy

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

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

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private var isTwoStepVerificationEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        initialize()
    }

    private fun initialize() {
        findViews()
        setUpOnClickListeners()
        loadTwoStepVerificationState()
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
            toggleTwoStepVerification()
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

        deleteAccountCard.setOnClickListener {
            showDeleteAccountConfirmationDialog()
        }
    }

    private fun loadTwoStepVerificationState() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                isTwoStepVerificationEnabled = document.getBoolean("twoStepVerificationEnabled") ?: false
                if (isTwoStepVerificationEnabled) {
                    val verificationCode = document.getString("twoStepVerificationCode")
                    if (!verificationCode.isNullOrEmpty()) {
                        // If two-step verification is enabled and the code exists, just update the UI
                        updateTwoStepVerificationUI()
                    } else {
                        // If no code is found, treat it as disabled
                        isTwoStepVerificationEnabled = false
                        updateTwoStepVerificationUI()
                    }
                } else {
                    updateTwoStepVerificationUI()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load two-step verification status: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun toggleTwoStepVerification() {
        val title = if (isTwoStepVerificationEnabled) "Disable Two-Step Verification" else "Enable Two-Step Verification"
        val message = if (isTwoStepVerificationEnabled) {
            "Are you sure you want to disable two-step verification? This action will make your account less secure."
        } else {
            "Are you sure you want to enable two-step verification? This action will make your account more secure."
        }

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes") { _, _ ->
                if (!isTwoStepVerificationEnabled) {
                    isTwoStepVerificationEnabled = true
                    updateTwoStepVerificationUI()
                    saveTwoStepVerificationState()
                    redirectToVerificationCodeActivity()
                } else {
                    isTwoStepVerificationEnabled = false
                    updateTwoStepVerificationUI()
                    saveTwoStepVerificationState()
                    Toast.makeText(this, "Two-Step Verification Disabled", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun redirectToVerificationCodeActivity() {
        Toast.makeText(this, "Two-Step Verification Enabled", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, VerificationCodeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateTwoStepVerificationUI() {
        if (isTwoStepVerificationEnabled) {
            twoStepVerificationCard.setCardBackgroundColor(Color.GREEN)
        } else {
            twoStepVerificationCard.setCardBackgroundColor(Color.RED)
        }
    }

    private fun saveTwoStepVerificationState() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .update("twoStepVerificationEnabled", isTwoStepVerificationEnabled)
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update two-step verification status: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDeleteAccountConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
            .setPositiveButton("Yes") { _, _ ->
                deleteAccount()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteAccount() {
        val userId = auth.currentUser?.uid ?: return
        val user = auth.currentUser ?: return
        val storageRef = storage.reference.child("profile_pictures/$userId/profile_picture.jpg")

        user.delete()
            .addOnSuccessListener {
                db.collection("users").document(userId)
                    .delete()
                    .addOnSuccessListener {
                        storageRef.delete()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                                startActivity(Intent(this, SignUpActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to delete profile picture: ${e.message}", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                                startActivity(Intent(this, SignUpActivity::class.java))
                                finish()
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to delete user data from Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                        auth.signOut()
                        startActivity(Intent(this, SignUpActivity::class.java))
                        finish()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to delete user from Firebase Authentication: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
