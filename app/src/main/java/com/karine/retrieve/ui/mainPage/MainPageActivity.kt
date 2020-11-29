package com.karine.retrieve.ui.mainPage

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager

import com.google.android.material.tabs.TabLayout
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityMainPageBinding


class MainPageActivity : AppCompatActivity() {

    private lateinit var mainPageBinding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainPageBinding = ActivityMainPageBinding.inflate(layoutInflater)
        val view = mainPageBinding.root
        setContentView(view)

        configureToolbar()
        configureViewPagerAndTabs()



    }
    //for toolbar
    private fun configureToolbar() {
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun configureViewPagerAndTabs() {
        //Get ViewPager from layout
        val pager = findViewById<View>(R.id.viewPager) as ViewPager
//        Get TabLayout from layout
        val tabs = findViewById<View>(R.id.tabLayout) as TabLayout
        // Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager)
        // Design purpose. Tabs have the same width
        tabs.tabMode = TabLayout.MODE_FIXED

        //Set Adapter PageAdapter and glue it together
        pager.adapter = PageAdapter(supportFragmentManager);
    }
}