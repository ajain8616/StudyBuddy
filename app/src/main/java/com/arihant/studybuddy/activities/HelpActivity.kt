package com.arihant.studybuddy.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arihant.studybuddy.R

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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please read and accept the instructions.", Toast.LENGTH_SHORT).show()
            }
        }


    }
}