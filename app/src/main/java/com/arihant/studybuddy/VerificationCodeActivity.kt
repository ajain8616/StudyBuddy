package com.arihant.studybuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VerificationCodeActivity : AppCompatActivity() {

    private lateinit var etDigit1: EditText
    private lateinit var etDigit2: EditText
    private lateinit var etDigit3: EditText
    private lateinit var etDigit4: EditText
    private lateinit var etDigit5: EditText
    private lateinit var etDigit6: EditText
    private lateinit var btnSubmitVerification: Button
    private lateinit var btnVerification: Button
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code)

        // Initialize UI components
        etDigit1 = findViewById(R.id.etDigit1)
        etDigit2 = findViewById(R.id.etDigit2)
        etDigit3 = findViewById(R.id.etDigit3)
        etDigit4 = findViewById(R.id.etDigit4)
        etDigit5 = findViewById(R.id.etDigit5)
        etDigit6 = findViewById(R.id.etDigit6)
        btnSubmitVerification = findViewById(R.id.btnSubmitVerification)
        btnVerification = findViewById(R.id.btnVerification)

        // Check if two-step verification is enabled and configure UI accordingly
        checkTwoStepVerificationStatus()

        // Handle the click event for the submit button
        btnSubmitVerification.setOnClickListener {
            val verificationCode = getVerificationCode()
            if (verificationCode.isNotEmpty() && userId != null) {
                if (btnVerification.visibility == Button.GONE) {
                    // If verification is disabled, save the verification code
                    saveVerificationCode(verificationCode)
                    // Show btnVerification after saving the code and hide btnSubmitVerification
                    btnVerification.visibility = Button.VISIBLE
                    btnSubmitVerification.visibility = Button.GONE
                } else {
                    // If verification is enabled, verify the code
                    verifyCode(verificationCode)
                }
            } else {
                Toast.makeText(this, "Please enter a valid code", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle the click event for the verify button
        btnVerification.setOnClickListener {
            val verificationCode = getVerificationCode()
            if (verificationCode.isNotEmpty() && userId != null) {
                verifyCode(verificationCode)
            } else {
                Toast.makeText(this, "Please enter a valid code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getVerificationCode(): String {
        return etDigit1.text.toString() +
                etDigit2.text.toString() +
                etDigit3.text.toString() +
                etDigit4.text.toString() +
                etDigit5.text.toString() +
                etDigit6.text.toString()
    }

    private fun saveVerificationCode(code: String) {
        if (userId != null) {
            db.collection("users").document(userId)
                .update("twoStepVerificationCode", code)
                .addOnSuccessListener {
                    Toast.makeText(this, "Verification code saved successfully", Toast.LENGTH_SHORT).show()
                    // Show btnVerification after saving the code and hide btnSubmitVerification
                    btnVerification.visibility = Button.VISIBLE
                    btnSubmitVerification.visibility = Button.GONE
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save verification code", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun verifyCode(code: String) {
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val storedCode = document.getString("twoStepVerificationCode")
                    if (code == storedCode) {
                        Toast.makeText(this, "Verification successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        // Proceed with further actions after successful verification
                    } else {
                        Toast.makeText(this, "Verification failed", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to verify code", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun checkTwoStepVerificationStatus() {
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val isTwoStepEnabled = document.getBoolean("twoStepVerificationEnabled") ?: false
                    val storedCode = document.getString("twoStepVerificationCode")

                    if (isTwoStepEnabled) {
                        btnVerification.visibility = Button.VISIBLE
                    } else {
                        btnVerification.visibility = Button.GONE
                        // Prompt user to enter and submit a code even if verification is disabled
                        if (storedCode.isNullOrEmpty()) {
                            Toast.makeText(this, "Two-Step Verification is disabled, but please enter a code", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to retrieve Two-Step Verification status", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
