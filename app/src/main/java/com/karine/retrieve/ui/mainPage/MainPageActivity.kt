package com.karine.retrieve.ui.mainPage


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityMainPageBinding
import com.karine.retrieve.ui.findLostPage.FindLostActivity
import com.karine.retrieve.ui.search.SearchActivity
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView.OnActionSelectedListener


class MainPageActivity : AppCompatActivity() {

    private lateinit var mainPageBinding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainPageBinding = ActivityMainPageBinding.inflate(layoutInflater)
        val view = mainPageBinding.root

        setContentView(view)

        configureToolbar()
        configureViewPagerAndTabs()
        clickAddBtn()

        //for fab btn add
        mainPageBinding.fabBtn.inflate(R.menu.fab_speed_dial);
    }

    private fun clickAddBtn () {
        mainPageBinding.fabBtn.setOnActionSelectedListener(OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.find-> {

                    val findLostIntent = Intent(this, FindLostActivity::class.java)
                    startActivity(findLostIntent)
                }
                R.id.lost -> {
                    val findLostIntent = Intent(this, FindLostActivity::class.java)
                    startActivity(findLostIntent)
                }
            }
            true
        })
    }

    //for toolbar
    private fun configureToolbar() {
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun configureViewPagerAndTabs() {
        //Get ViewPager from layout
        val pager = mainPageBinding.viewPager
//        Get TabLayout from layout
        val tabs = mainPageBinding.tabLayout
        // Glue TabLayout and ViewPager together
        pager.adapter = PageAdapter(this);
//        // Design purpose. Tabs have the same width
        tabs.tabMode = TabLayout.MODE_FIXED

        TabLayoutMediator(
            tabs, pager
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> tab.text = "Objets trouvés"
                1 -> tab.text = "Objets perdus"
            }
        }.attach()

        if (tabs.selectedTabPosition == 0) {
//            mainPageBinding.fabBtn.show()
        }
        if (tabs.selectedTabPosition == 1) {
//                mainPageBinding.fabBtn.show()
//        } else {
//            mainPageBinding.fabBtn.hide()
        }
    }





}

