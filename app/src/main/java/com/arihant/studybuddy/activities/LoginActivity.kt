package com.arihant.studybuddy.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.arihant.studybuddy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPassword: TextView
    private lateinit var signupPrompt: TextView
    private lateinit var verificationLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)
        forgotPassword = findViewById(R.id.forgotPassword)
        signupPrompt = findViewById(R.id.signupPrompt)
        verificationLink = findViewById(R.id.verificationLink)

        checkEmailVerificationStatus()

        loginButton.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()

            if (validateInputs(emailText, passwordText)) {
                loginUser(emailText, passwordText)
            }
        }

        forgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        signupPrompt.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        verificationLink.setOnClickListener {
            sendEmailVerification()
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            this.email.error = "Email is required"
            this.email.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            this.password.error = "Password is required"
            this.password.requestFocus()
            return false
        }

        if (password.length < 9) {
            this.password.error = "Password should be at least 9 characters"
            this.password.requestFocus()
            return false
        }

        return true
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Check two-step verification status after successful login
                    checkTwoStepVerificationStatus()
                } else {
                    Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkTwoStepVerificationStatus() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val isTwoStepEnabled = document.getBoolean("twoStepVerificationEnabled") ?: false
                        if (isTwoStepEnabled) {
                            redirect("VERIFICATION")
                        } else {
                            redirect("MAIN")
                        }
                    } else {
                        Toast.makeText(this, "User document not found.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun redirect(name: String) {
        val intent = Intent(
            this, when (name) {
                "MAIN" -> MainActivity::class.java
                "VERIFICATION" -> VerificationCodeActivity::class.java
                else -> throw Exception("No path exists")
            }
        )
        startActivity(intent)
        finish()
    }

    private fun checkEmailVerificationStatus() {
        val user = auth.currentUser
        if (user != null) {
            if (user.isEmailVerified) {
                verificationLink.visibility = View.GONE
            } else {
                verificationLink.visibility = View.VISIBLE
            }
        }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        if (user != null && !user.isEmailVerified) {
            user.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Verification email sent to ${user.email}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to send verification email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun showForgotPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Forgot Password")
        val view = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        builder.setView(view)
        builder.setPositiveButton("Reset") { _, _ ->
            val email = emailEditText.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                resetPassword(email)
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
