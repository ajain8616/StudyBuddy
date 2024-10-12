package com.arihant.studybuddy

import android.os.Bundle
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo.RangeInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PrivacyActivity : AppCompatActivity() {

    // Declare all views as private lateinit variables
    private lateinit var layoutLastSeen: RelativeLayout
    private lateinit var mainRadioLinearLayout: LinearLayout
    private lateinit var rbEveryone: RadioButton
    private lateinit var rbMyContacts: RadioButton
    private lateinit var rbMyContactsExcept: RadioButton
    private lateinit var rbNobody: RadioButton
    private lateinit var rbSameAsLastSeen: RadioButton
    private lateinit var rbNobodyOnline: RadioButton
    private lateinit var layoutProfilePhoto: RelativeLayout
    private lateinit var imageProfilePhoto: ImageView
    private lateinit var textProfilePhoto: TextView
    private lateinit var hintProfilePhoto: TextView
    private lateinit var layoutProfilePhotoVisibility: LinearLayout
    private lateinit var rbProfileEveryone: RadioButton
    private lateinit var rbProfileMyContacts: RadioButton
    private lateinit var rbProfileMyContactsExcept: RadioButton
    private lateinit var rbProfileNobody: RadioButton
    private lateinit var layoutStatus: RelativeLayout
    private lateinit var imageStatus: ImageView
    private lateinit var textStatus: TextView
    private lateinit var hintStatus: TextView
    private lateinit var layoutStatusVisibility: LinearLayout
    private lateinit var rbStatusEveryone: RadioButton
    private lateinit var rbStatusMyContacts: RadioButton
    private lateinit var rbStatusMyContactsExcept: RadioButton
    private lateinit var rbStatusNobody: RadioButton
    private lateinit var layoutReadReceipts: RelativeLayout
    private lateinit var imageReadReceipts: ImageView
    private lateinit var textReadReceipts: TextView
    private lateinit var hintReadReceipts: TextView
    private lateinit var layoutDisappearingMessages: RelativeLayout
    private lateinit var imageDisappearingMessages: ImageView
    private lateinit var textDisappearingMessages: TextView
    private lateinit var hintDisappearingMessages: TextView
    private lateinit var layoutDMVisibility:LinearLayout
    private lateinit var rd24hours:RadioButton
    private lateinit var rdrd7days:RadioButton
    private lateinit var rd90days:RadioButton
    private lateinit var rbOff:RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        // Initialize all views
        initializeViews()

        // Set click listeners
        setClickListeners()
    }

    private fun initializeViews() {
        layoutLastSeen = findViewById(R.id.layoutLastSeen)
        mainRadioLinearLayout = findViewById(R.id.mainRadioLinearLayout)
        rbEveryone = findViewById(R.id.rbEveryone)
        rbMyContacts = findViewById(R.id.rbMyContacts)
        rbMyContactsExcept = findViewById(R.id.rbMyContactsExcept)
        rbNobody = findViewById(R.id.rbNobody)
        rbSameAsLastSeen = findViewById(R.id.rbSameAsLastSeen)
        rbNobodyOnline = findViewById(R.id.rbNobodyOnline)
        layoutProfilePhoto = findViewById(R.id.layoutProfilePhoto)
        imageProfilePhoto = findViewById(R.id.imageProfilePhoto)
        textProfilePhoto = findViewById(R.id.textProfilePhoto)
        hintProfilePhoto = findViewById(R.id.hintProfilePhoto)
        layoutProfilePhotoVisibility = findViewById(R.id.layoutProfilePhotoVisibility)
        rbProfileEveryone = findViewById(R.id.rbProfileEveryone)
        rbProfileMyContacts = findViewById(R.id.rbProfileMyContacts)
        rbProfileMyContactsExcept = findViewById(R.id.rbProfileMyContactsExcept)
        rbProfileNobody = findViewById(R.id.rbProfileNobody)
        layoutStatus = findViewById(R.id.layoutStatus)
        imageStatus = findViewById(R.id.imageStatus)
        textStatus = findViewById(R.id.textStatus)
        hintStatus = findViewById(R.id.hintStatus)
        layoutStatusVisibility = findViewById(R.id.layoutStatusVisibility)
        rbStatusEveryone = findViewById(R.id.rbStatusEveryone)
        rbStatusMyContacts = findViewById(R.id.rbStatusMyContacts)
        rbStatusMyContactsExcept = findViewById(R.id.rbStatusMyContactsExcept)
        rbStatusNobody = findViewById(R.id.rbStatusNobody)
        layoutReadReceipts = findViewById(R.id.layoutReadReceipts)
        imageReadReceipts = findViewById(R.id.imageReadReceipts)
        textReadReceipts = findViewById(R.id.textReadReceipts)
        hintReadReceipts = findViewById(R.id.hintReadReceipts)
        layoutDisappearingMessages = findViewById(R.id.layoutDisappearingMessages)
        imageDisappearingMessages = findViewById(R.id.imageDisappearingMessages)
        textDisappearingMessages = findViewById(R.id.textDisappearingMessages)
        hintDisappearingMessages = findViewById(R.id.hintDisappearingMessages)
        layoutDMVisibility = findViewById(R.id.layoutDMVisibility)
        rdrd7days = findViewById(R.id.rd7days)
        rd90days = findViewById(R.id.rd90days)
        rd24hours = findViewById(R.id.rd24hours)
        rbOff = findViewById(R.id.rbOff)
    }

    private fun setClickListeners() {
        layoutLastSeen.setOnClickListener {
            toggleSpecificLayout(mainRadioLinearLayout)
            toggleVisibility(
                layoutProfilePhoto,
                layoutStatus,
                layoutReadReceipts,
                layoutDisappearingMessages
            )
        }

        layoutProfilePhoto.setOnClickListener {
            toggleSpecificLayout(layoutProfilePhotoVisibility)
            toggleVisibility(
                layoutLastSeen,
                layoutStatus,
                layoutReadReceipts,
                layoutDisappearingMessages
            )
        }

        layoutStatus.setOnClickListener {
            toggleSpecificLayout(layoutStatusVisibility)
            toggleVisibility(
                layoutLastSeen,
                layoutProfilePhoto,
                layoutReadReceipts,
                layoutDisappearingMessages
            )
        }

        layoutReadReceipts.setOnClickListener {
            // Add functionality here if needed
        }

        layoutDisappearingMessages.setOnClickListener {
            toggleSpecificLayout(layoutDMVisibility)
            toggleVisibility(
                layoutLastSeen,
                layoutProfilePhoto,
                layoutStatus,
                layoutReadReceipts
            )

        }
    }

    private fun toggleVisibility(vararg layoutsToHide: View) {
        val isVisible = layoutsToHide.any { it.visibility == View.VISIBLE }
        if (isVisible) {
            layoutsToHide.forEach { it.visibility = View.GONE }
        } else {
            layoutsToHide.forEach { it.visibility = View.VISIBLE }
        }
    }

    private fun toggleSpecificLayout(layout: View) {
        if (layout.visibility == View.VISIBLE) {
            layout.visibility = View.GONE
        } else {
            layout.visibility = View.VISIBLE
        }
    }
}
