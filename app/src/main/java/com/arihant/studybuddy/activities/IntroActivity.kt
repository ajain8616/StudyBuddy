package com.arihant.studybuddy.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arihant.studybuddy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class IntroActivity : AppCompatActivity() {

    private lateinit var btnGetStarted: Button
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        btnGetStarted = findViewById(R.id.btnGetStarted)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Check if the user document exists
            db.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val isTwoStepEnabled = document.getBoolean("twoStepVerificationEnabled") ?: false
                        if (isTwoStepEnabled) {
                            // Redirect to VerificationCodeActivity
                            redirect("VERIFICATION")
                        } else {
                            // Redirect to MainActivity
                            redirect("MAIN")
                        }
                    } else {
                        // User document does not exist; handle as needed
                        Toast.makeText(this, "User document not found.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                }
        } else {
            // No user is logged in; show the login button
            btnGetStarted.setOnClickListener {
                redirect("LOGIN")
            }
        }
    }

    private fun redirect(name: String) {
        val intent = Intent(
            this, when (name) {
                "LOGIN" -> LoginActivity::class.java
                "MAIN" -> MainActivity::class.java
                "VERIFICATION" -> VerificationCodeActivity::class.java
                else -> throw Exception("No path exists")
            }
        )
        startActivity(intent)
        finish()
    }
}
