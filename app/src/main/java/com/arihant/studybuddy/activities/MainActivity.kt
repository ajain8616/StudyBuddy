package com.arihant.studybuddy.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.arihant.studybuddy.fragments.MessagesFragment
import com.arihant.studybuddy.fragments.ProfileFragment
import com.arihant.studybuddy.R
import com.arihant.studybuddy.fragments.SubjectListFragment
import com.arihant.studybuddy.fragments.CommunitiesFragment
import com.arihant.studybuddy.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.home -> selectedFragment = HomeFragment()
                R.id.subject_list -> selectedFragment = SubjectListFragment()
                R.id.communities -> selectedFragment = CommunitiesFragment()
                R.id.message -> selectedFragment = MessagesFragment()
                R.id.profile -> selectedFragment = ProfileFragment()
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it)
                    .commit()
            }
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.home
    }

    override fun onBackPressed() {
        // Check if any fragment in the back stack
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
