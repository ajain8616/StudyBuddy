package com.arihant.studybuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HelpActivity : AppCompatActivity() {

    private lateinit var checkBox:CheckBox
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        checkBox = findViewById(R.id.instructionCheckBox)
        startButton = findViewById(R.id.startTestButton)

        startButton.setOnClickListener {
            if (checkBox.isChecked) {
                val homeFragment = HomeFragment()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, homeFragment)
                    addToBackStack(null)
                    commit()
                }
            } else {
                Toast.makeText(this, "Please read and accept the instructions.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}