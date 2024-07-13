package com.arihant.studybuddy

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var userTypeGroup: RadioGroup
    private lateinit var signUpButton: Button
    private lateinit var loginPrompt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        fullName = findViewById(R.id.fullName)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        userTypeGroup = findViewById(R.id.userTypeGroup)
        signUpButton = findViewById(R.id.signupButton)
        loginPrompt = findViewById(R.id.loginPrompt)

        signUpButton.setOnClickListener {
            val name = fullName.text.toString().trim()
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()
            val confirmPasswordText = confirmPassword.text.toString().trim()
            val userType = when (userTypeGroup.checkedRadioButtonId) {
                R.id.schoolStudent -> "School Student"
                R.id.collegeStudent -> "College Student"
                R.id.workingProfessional -> "Working Professional"
                else -> ""
            }

            if (validateInputs(name, emailText, passwordText, confirmPasswordText, userType)) {
                signUpUser(name, emailText, passwordText, userType)
            }

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginPrompt.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateInputs(
        name: String, email: String, password: String,
        confirmPassword: String, userType: String
    ): Boolean {
        if (name.isEmpty()) {
            fullName.error = "Full Name is required"
            fullName.requestFocus()
            return false
        }

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

        if (password != confirmPassword) {
            this.confirmPassword.error = "Passwords do not match"
            this.confirmPassword.requestFocus()
            return false
        }

        if (userType.isEmpty()) {
            Toast.makeText(this, "Please select a user type", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun signUpUser(name: String, email: String, password: String, userType: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                val userId = user.uid
                                val userMap = hashMapOf(
                                    "fullName" to name,
                                    "email" to email,
                                    "userType" to userType
                                )

                                firestore.collection("users").document(userId).set(userMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Sign Up Successful. Please verify your email.", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to store user details: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "Failed to send verification email: ${verificationTask.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

