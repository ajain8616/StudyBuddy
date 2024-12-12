package com.arihant.studybuddy.adapters

import android.graphics.Bitmap
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.arihant.studybuddy.activities.AvatarActivity
import com.arihant.studybuddy.R
import com.arihant.studybuddy.models.Avatar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

class AvatarAdapter(private val context: AvatarActivity, private val avatars: List<Avatar>) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_avatar, parent, false)
        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatar = avatars[position]
        holder.avatarName.text = avatar.name
        holder.avatarImage.setImageBitmap(avatar.image)

        // Set up the click listener for the item view
        holder.itemView.setOnClickListener {
            // Upload the selected avatar to Firebase Storage
            val user = auth.currentUser
            user?.let {
                val userId = it.uid
                val storageRef = storage.reference.child("profile_pictures/$userId/profile_picture.jpg")

                // Convert the Bitmap to a byte array
                val baos = ByteArrayOutputStream()
                avatar.image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                // Upload the byte array to Firebase Storage
                val uploadTask = storageRef.putBytes(data)
                uploadTask.addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val imageUrl = downloadUrl.toString()

                        // Update the Firestore document with the new image URL
                        val userMap = hashMapOf(
                            "imageUrl" to imageUrl
                        )

                        db.collection("users").document(userId)
                            .update(userMap as Map<String, Any>)
                            .addOnSuccessListener {
                                Toast.makeText(holder.itemView.context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(holder.itemView.context, "Failed to update URL in Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(holder.itemView.context, "Failed to get download URL: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(holder.itemView.context, "Failed to upload avatar: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return avatars.size
    }

    class AvatarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarName: TextView = itemView.findViewById(R.id.avatarName)
        val avatarImage: ImageView = itemView.findViewById(R.id.avatarImage)
    }
}
