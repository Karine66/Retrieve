package com.karine.retrieve.ui.mainPage


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityMainPageBinding
import com.karine.retrieve.ui.listPage.FindLostFragment


class MainPageActivity : AppCompatActivity() {

    private lateinit var mainPageBinding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainPageBinding = ActivityMainPageBinding.inflate(layoutInflater)
        val view = mainPageBinding.root

        setContentView(view)

        configureToolbar()
        configureViewPagerAndTabs()
        onClickFab()

    }

    fun onClickFab() {
        mainPageBinding.fabBtn.setOnClickListener(View.OnClickListener {
            val fabIntent = Intent(applicationContext, FindLostFragment::class.java)
            startActivity(fabIntent)
        })
    }


    //for toolbar
    private fun configureToolbar() {
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun configureViewPagerAndTabs() {
        //Get ViewPager from layout
        val pager = findViewById<View>(R.id.viewPager) as ViewPager2
//        Get TabLayout from layout
        val tabs = findViewById<View>(R.id.tabLayout) as TabLayout
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

//        if (tabs.selectedTabPosition == 0) {
//            mainPageBinding.fabBtn.show()
//            if (tabs.selectedTabPosition == 1)
//                mainPageBinding.fabBtn.show()
//        } else {
//            mainPageBinding.fabBtn.hide()
//
//
//        }

    }
}
