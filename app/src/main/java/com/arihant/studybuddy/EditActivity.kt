package com.arihant.studybuddy

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class EditActivity : AppCompatActivity() {

    private lateinit var imageProfile: ImageView
    private lateinit var title: TextView
    private lateinit var editFullName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editRole: RadioGroup
    private lateinit var rbSchoolStudent: RadioButton
    private lateinit var rbCollegeStudent: RadioButton
    private lateinit var rbWorkingProfessional: RadioButton
    private lateinit var btnUpdateProfile: Button

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private var imageUri: Uri? = null

    companion object {
        private const val REQUEST_CODE_IMAGE_PICK = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        auth = Firebase.auth

        imageProfile = findViewById(R.id.imageProfile)
        title = findViewById(R.id.title)
        editFullName = findViewById(R.id.editFullName)
        editEmail = findViewById(R.id.editEmail)
        editRole = findViewById(R.id.editRole)
        rbSchoolStudent = findViewById(R.id.rbSchoolStudent)
        rbCollegeStudent = findViewById(R.id.rbCollegeStudent)
        rbWorkingProfessional = findViewById(R.id.rbWorkingProfessional)
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)

        fetchUserData()

        imageProfile.setOnClickListener {
            pickImageFromGallery()
        }

        btnUpdateProfile.setOnClickListener {
            val fullName = editFullName.text.toString()
            val email = editEmail.text.toString()
            val selectedRoleId = editRole.checkedRadioButtonId
            val role = when (selectedRoleId) {
                R.id.rbSchoolStudent -> "School Student"
                R.id.rbCollegeStudent -> "College Student"
                R.id.rbWorkingProfessional -> "Working Professional"
                else -> ""
            }
            imageUri?.let { uri ->
                uploadImageAndSaveProfile(uri, fullName, email, role)
            } ?: run {
                Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUserData() {
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val fullName = document.getString("fullName")
                        val email = document.getString("email")
                        val userType = document.getString("userType")
                        val imageUrl = document.getString("imageUrl")

                        editFullName.setText(fullName)
                        editEmail.setText(email)
                        when (userType) {
                            "School Student" -> rbSchoolStudent.isChecked = true
                            "College Student" -> rbCollegeStudent.isChecked = true
                            "Working Professional" -> rbWorkingProfessional.isChecked = true
                        }
                        imageUrl?.let { url ->
                            Glide.with(this).load(url).into(imageProfile)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error fetching user data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageProfile.setImageURI(imageUri)
        }
    }

    private fun uploadImageAndSaveProfile(uri: Uri, fullName: String, email: String, role: String) {
        val user = auth.currentUser

        user?.let {
            val userId = it.uid
            val storageRef = storage.reference.child("profile_pictures/$userId/profile_picture.jpg")

            val uploadTask = storageRef.putFile(uri)
            uploadTask.addOnSuccessListener { _ ->
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val imageUrl = downloadUrl.toString()

                    val userMap = hashMapOf(
                        "fullName" to fullName,
                        "email" to email,
                        "userType" to role,
                        "imageUrl" to imageUrl
                    )

                    db.collection("users").document(userId)
                        .set(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error updating profile: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
