package com.karine.retrieve.ui.mainPage


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityMainPageBinding
import com.karine.retrieve.ui.findLostPage.FindLostActivity
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView.OnActionSelectedListener


class MainPageActivity : AppCompatActivity() {

    private lateinit var mainPageBinding: ActivityMainPageBinding
//    private val tabs:TabLayout = mainPageBinding.tabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainPageBinding = ActivityMainPageBinding.inflate(layoutInflater)
        val view = mainPageBinding.root

        setContentView(view)

        configureToolbar()
        configureViewPagerAndTabs()
        clickAddBtn()
        btnSpeedDial()
        showHideFabTabs()


    }
    //for create personalize btn Speed Dial
    private fun btnSpeedDial() {

        mainPageBinding.fabBtn.addActionItem(
            SpeedDialActionItem.Builder(R.id.fab_find, R.drawable.outline_add_white_24dp)
                .setLabel(R.string.trouve)
                .setLabelColor(ContextCompat.getColor(this, R.color.colorPrimaryVariant))
                .create()
        )

        mainPageBinding.fabBtn.addActionItem(
            SpeedDialActionItem.Builder(R.id.fab_lost, R.drawable.outline_add_white_24dp)
                .setLabel(R.string.perdu)
                .setLabelColor(ContextCompat.getColor(this, R.color.colorPrimaryVariant))
                .create()
        )
    }

    //For click on fab button spedd dial
    private fun clickAddBtn () {
        mainPageBinding.fabBtn.setOnActionSelectedListener(OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.fab_find -> {
                    val findLostIntent = Intent(this, FindLostActivity::class.java)
                    startActivity(findLostIntent)
                }
                R.id.fab_lost -> {
                    val findLostIntent = Intent(this, FindLostActivity::class.java)
                    startActivity(findLostIntent)
                }
            }
            true
        })
    }

    //for hide or show fab button between tabs
    private fun showHideFabTabs() {
        mainPageBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager2.SCROLL_STATE_IDLE -> mainPageBinding.fabBtn.show()
                    ViewPager2.SCROLL_STATE_DRAGGING, ViewPager2.SCROLL_STATE_SETTLING -> mainPageBinding.fabBtn.hide()
                }
            }
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
//        tabs.tabMode = TabLayout.MODE_FIXED

        TabLayoutMediator(
            tabs, pager
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> tab.text = "Objets trouvés"
                1 -> tab.text = "Objets perdus"
            }
        }.attach()
        //for display fab button
        if(tabs.selectedTabPosition == 0 ) {
            mainPageBinding.fabBtn.show()
        }
    }

}

