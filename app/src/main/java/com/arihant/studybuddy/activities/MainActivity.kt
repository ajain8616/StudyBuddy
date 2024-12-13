package com.arihant.studybuddy.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.arihant.studybuddy.R
import com.arihant.studybuddy.fragments.CommunitiesFragment
import com.arihant.studybuddy.fragments.HomeFragment
import com.arihant.studybuddy.fragments.MessagesFragment
import com.arihant.studybuddy.fragments.ProfileFragment
import com.arihant.studybuddy.fragments.SubjectListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Set the ViewPager adapter
        viewPager.adapter = ViewPagerAdapter(this)

        // Bottom Navigation item selection handling
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> viewPager.currentItem = 0
                R.id.subject_list -> viewPager.currentItem = 1
                R.id.communities -> viewPager.currentItem = 2
                R.id.message -> viewPager.currentItem = 3
                R.id.profile -> viewPager.currentItem = 4
            }
            true
        }

        // Sync ViewPager with BottomNavigationView
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> bottomNavigationView.selectedItemId = R.id.home
                    1 -> bottomNavigationView.selectedItemId = R.id.subject_list
                    2 -> bottomNavigationView.selectedItemId = R.id.communities
                    3 -> bottomNavigationView.selectedItemId = R.id.message
                    4 -> bottomNavigationView.selectedItemId = R.id.profile
                }
            }
        })

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

    private inner class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 5 // Total fragments
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> SubjectListFragment()
                2 -> CommunitiesFragment()
                3 -> MessagesFragment()
                4 -> ProfileFragment()
                else -> HomeFragment()
            }
        }
    }
}
