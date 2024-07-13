package com.arihant.studybuddy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var roleTextView: TextView
    private lateinit var aboutButton: ImageView
    private lateinit var editButton: ImageView
    private lateinit var settingsButton: ImageView
    private lateinit var helpButton: RelativeLayout
    private lateinit var logoutButton: RelativeLayout

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize the views
        profileImageView = view.findViewById(R.id.profileImageViewProfile)
        usernameTextView = view.findViewById(R.id.usernameProfile)
        emailTextView = view.findViewById(R.id.tvEmailHintProfile)
        roleTextView = view.findViewById(R.id.tvRoleHintProfile)
        aboutButton = view.findViewById(R.id.aboutButtonProfile)
        editButton = view.findViewById(R.id.editButtonProfile)
        settingsButton = view.findViewById(R.id.settingsButtonProfile)
        helpButton = view.findViewById(R.id.stThirdLayoutProfile)
        logoutButton = view.findViewById(R.id.stFourthLayoutProfile)

        // Set up click listeners for the buttons
        aboutButton.setOnClickListener { onAboutButtonClick() }
        editButton.setOnClickListener { onEditButtonClick() }
        settingsButton.setOnClickListener { onSettingsButtonClick() }
        helpButton.setOnClickListener { onHelpButtonClick() }
        logoutButton.setOnClickListener { onLogoutButtonClick() }

        // Fetch user details from Firestore
        fetchUserDetails()

        return view
    }

    private fun fetchUserDetails() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        usernameTextView.text = document.getString("fullName")
                        emailTextView.text = document.getString("email")
                        roleTextView.text = document.getString("userType")

                        // Load profile image using Glide
                        val imageUrl = document.getString("imageUrl")
                        if (!imageUrl.isNullOrEmpty()) {
                            Glide.with(requireContext())
                                .load(imageUrl)
                                .into(profileImageView)
                        }

                    } else {
                        Toast.makeText(context, "No such user found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error getting user details: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun onAboutButtonClick() {
        val intent = Intent(activity, AboutActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun onEditButtonClick() {
        val intent = Intent(activity, EditActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun onSettingsButtonClick() {
        val intent = Intent(activity, SettingsActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun onHelpButtonClick() {
        val intent = Intent(activity, HelpActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun onLogoutButtonClick() {
        auth.signOut()
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}
