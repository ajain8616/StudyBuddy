package com.arihant.studybuddy.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arihant.studybuddy.R
import com.arihant.studybuddy.adapters.MessageAdapter
import com.arihant.studybuddy.models.Message
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MessagesFragment : Fragment() {

    private lateinit var linearLayoutHeader: LinearLayout
    private lateinit var textViewAppName: TextView
    private lateinit var imageViewSearchIcon: ImageView
    private lateinit var imageViewFilterIcon: ImageView
    private lateinit var editTextSearch: EditText
    private lateinit var linearLayoutCategories: LinearLayout
    private lateinit var recyclerViewMessages: RecyclerView
    private lateinit var fabNewMessage: FloatingActionButton
    private lateinit var mAuth: FirebaseAuth

    private var isSearchVisible = false
    private var isCategoriesVisible = false

    private lateinit var messageAdapter: MessageAdapter
    private val messagesPersons = mutableListOf<Message>()

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutHeader = view.findViewById(R.id.linearLayoutHeader)
        textViewAppName = view.findViewById(R.id.textViewAppName)
        imageViewSearchIcon = view.findViewById(R.id.imageViewSearchIcon)
        imageViewFilterIcon = view.findViewById(R.id.imageViewFilterIcon)
        editTextSearch = view.findViewById(R.id.editTextSearch)
        linearLayoutCategories = view.findViewById(R.id.linearLayoutCategories)
        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages)
        fabNewMessage = view.findViewById(R.id.fabNewMessage)

        mAuth = FirebaseAuth.getInstance()
        messageAdapter = MessageAdapter(messagesPersons)
        recyclerViewMessages.layoutManager = LinearLayoutManager(context)
        recyclerViewMessages.adapter = messageAdapter

        imageViewSearchIcon.setOnClickListener {
            toggleSearchVisibility()
        }

        imageViewFilterIcon.setOnClickListener {
            toggleCategoriesVisibility()
        }

        editTextSearch.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                if (event.rawX >= (editTextSearch.right - editTextSearch.compoundDrawables[drawableEnd].bounds.width())) {
                    editTextSearch.text.clear()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        fabNewMessage.setOnClickListener {

        }

        // Fetch user data from Firebase Firestore
        fetchUserData()
    }

    private fun toggleSearchVisibility() {
        if (isSearchVisible) {
            editTextSearch.visibility = View.GONE
        } else {
            editTextSearch.visibility = View.VISIBLE
        }
        isSearchVisible = !isSearchVisible
    }

    private fun toggleCategoriesVisibility() {
        if (isCategoriesVisible) {
            linearLayoutCategories.visibility = View.GONE
        } else {
            linearLayoutCategories.visibility = View.VISIBLE
        }
        isCategoriesVisible = !isCategoriesVisible
    }

    private fun fetchUserData() {
        val currentUserUid = mAuth.currentUser?.uid // Get the current user's UID

        db.collection("users").get()
            .addOnSuccessListener { result ->
                messagesPersons.clear()
                for (document in result) {
                    val fullName = document.getString("fullName")
                    val imageUrl = document.getString("imageUrl")
                    val userUid = document.id

                    // Add condition to exclude the current user's data
                    if (userUid != currentUserUid) {
                        // Create a Message object for each user
                        if (fullName != null && imageUrl != null) {
                            val message = Message(
                                senderUid = userUid,
                                senderName = fullName,
                                message = "Hello, how are you?",
                                time = "12:45 PM",
                                profileImageUrl = imageUrl,
                                messageCount = 0
                            )
                            messagesPersons.add(message)
                        }
                    }
                }
                messageAdapter.notifyDataSetChanged() // Notify adapter of data change
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error fetching user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
