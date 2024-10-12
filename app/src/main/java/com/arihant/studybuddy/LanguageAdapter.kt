package com.arihant.studybuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LanguageAdapter(private val languages: List<Languages>) :
    RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val languageIcon: ImageView = itemView.findViewById(R.id.languageIcon)
        val languageNameTextView: TextView = itemView.findViewById(R.id.languageNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]
        holder.languageNameTextView.text = language.name
        holder.languageIcon.setImageResource(language.iconResId)
    }

    override fun getItemCount(): Int {
        return languages.size
    }
}
