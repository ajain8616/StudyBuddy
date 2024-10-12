package com.arihant.studybuddy

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LanguageActivity : AppCompatActivity() {

    private lateinit var languageRecyclerView: RecyclerView
    private lateinit var languageAdapter: LanguageAdapter
    private val languages = mutableListOf<Languages>()
    private lateinit var languageGenerateButton: Button
    private lateinit var languageProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        // Initialize views
        languageRecyclerView = findViewById(R.id.languageRecyclerView)
        languageGenerateButton = findViewById(R.id.languageGenerateButton)
        languageProgressBar = findViewById(R.id.languageProgressBar)

        // Setup RecyclerView
        languageAdapter = LanguageAdapter(languages)
        languageRecyclerView.layoutManager = LinearLayoutManager(this)
        languageRecyclerView.adapter = languageAdapter

        // Set up button click listener
        languageGenerateButton.setOnClickListener {
            generateLanguages()
        }
    }

    private fun generateLanguages() {
        // Hide RecyclerView and show ProgressBar
        languageRecyclerView.visibility = View.GONE
        languageProgressBar.visibility = View.VISIBLE

        // Clear existing languages
        languages.clear()
        languageAdapter.notifyDataSetChanged()

        // Simulate a delay to represent loading or generating languages
        Handler(Looper.getMainLooper()).postDelayed({
            // Add new languages
            languages.add(Languages("English", R.drawable.ic_english))
            languages.add(Languages("Sanskrit", R.drawable.ic_sanskrit))
            languages.add(Languages("Hindi", R.drawable.ic_hindi))
            languages.add(Languages("Spanish", R.drawable.ic_spanish))
            languages.add(Languages("French", R.drawable.ic_french))
            languages.add(Languages("German", R.drawable.ic_german))
            languages.add(Languages("Chinese", R.drawable.ic_chinese))
            languages.add(Languages("Japanese", R.drawable.ic_japanese))
            languages.add(Languages("Russian", R.drawable.ic_russian))
            languages.add(Languages("Portuguese", R.drawable.ic_portuguese))
            languages.add(Languages("Korean", R.drawable.ic_korean))
            languages.add(Languages("Bengali", R.drawable.ic_bengali))
            languages.add(Languages("Tamil", R.drawable.ic_tamil))
            languages.add(Languages("Marathi", R.drawable.ic_marathi))
            languages.add(Languages("Gujarati", R.drawable.ic_gujarati))
            languages.add(Languages("Malayalam", R.drawable.ic_malayalam))
            languages.add(Languages("Kannada", R.drawable.ic_kannada))
            languages.add(Languages("Odia", R.drawable.ic_odia))
            languages.add(Languages("Punjabi", R.drawable.ic_punjabi))
            languages.add(Languages("Assamese", R.drawable.ic_assamese))

            // Notify adapter of data change
            languageAdapter.notifyDataSetChanged()

            // Show RecyclerView and hide ProgressBar
            languageRecyclerView.visibility = View.VISIBLE
            languageProgressBar.visibility = View.GONE
        }, 2000) // Adjust delay as needed
    }
}
