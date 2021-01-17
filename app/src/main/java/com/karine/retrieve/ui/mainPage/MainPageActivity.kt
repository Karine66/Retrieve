package com.karine.retrieve.ui.mainPage

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityMainPageBinding
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.ui.BaseActivity
import com.karine.retrieve.ui.MapBoxViewModel
import com.karine.retrieve.ui.UserObjectViewModel
import com.karine.retrieve.ui.findLostPage.FindLostActivity
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView.OnActionSelectedListener
import com.mapbox.mapboxsdk.Mapbox


class MainPageActivity() : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var mainPageBinding: ActivityMainPageBinding
    private lateinit var tabs: TabLayout
    private lateinit var pager: ViewPager2
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var userObjectViewModel: UserObjectViewModel
    private var userObject = UserObject()
    private val SIGN_OUT_TASK = 100
    private val DELETE_USER_TASK = 200
    private val anouncementList : MutableList<String> = mutableListOf()
    private val userUid = FirebaseAuth.getInstance().uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainPageBinding = ActivityMainPageBinding.inflate(layoutInflater)
        val view = mainPageBinding.root
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(view)

        configureToolbar()
        configureDrawerLayout()
        configureNavigationView()
        configureViewPagerAndTabs()
        methodRequiresFourPermission()
        clickAddBtn()
        btnSpeedDial()
        showHideFabTabs()
        updateUINavHeader()
//        configureViewModel()
       retrieveAnnouncementForUser()


//        for display fab button
        if (tabs.selectedTabPosition == 0) {
            mainPageBinding.fabBtn.show()
        }

    }

    //configure viewModel
    private fun configureViewModel() {
        userObjectViewModel = ViewModelProvider(this).get(UserObjectViewModel::class.java)
//        userObjectViewModel.getSavedUserObjectFind().observe(this, this::retrieveAnnouncementForUser)

    }

    override fun onBackPressed() {

        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //handle click on navigation view
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_drawer_find -> {
                pager.currentItem = 0
            }
            R.id.menu_drawer_lost -> {
                pager.currentItem = 1
            }
            R.id.menu_drawer_delete_account -> {
                onClickDelete()
            }
            R.id.menu_drawer_Logout -> {
                signOutUserFromFirebase()
                Snackbar.make(
                    mainPageBinding.root,
                    getString(R.string.deconnexion),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            else -> {
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    //Configure Drawer Layout
    private fun configureDrawerLayout() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        drawerLayout = (findViewById<View>(R.id.drawer_layout) as DrawerLayout)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    //Configure NavigationView
    private fun configureNavigationView() {
        this.navigationView = findViewById<View>(R.id.main_page_nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
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

    //For click on fab button speed dial
    private fun clickAddBtn() {
        mainPageBinding.fabBtn.setOnActionSelectedListener(OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.fab_find -> {
                    mainPageBinding.viewPager.currentItem = 0
                    val findIntent = Intent(this, FindLostActivity::class.java)
                    findIntent.putExtra("findClick", 0)
                    startActivity(findIntent)
                    mainPageBinding.fabBtn.close()
                    return@OnActionSelectedListener true

                }
                R.id.fab_lost -> {
                    mainPageBinding.viewPager.currentItem = 1
                    val lostIntent = Intent(this, FindLostActivity::class.java)
                    lostIntent.putExtra("lostClick", 1)
                    startActivity(lostIntent)
                    mainPageBinding.fabBtn.close()
                    return@OnActionSelectedListener true
                }
            }
            false
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

    private fun configureViewPagerAndTabs() {
        //Get ViewPager from layout
        pager = mainPageBinding.viewPager
//        Get TabLayout from layout
        tabs = mainPageBinding.tabLayout
        // Glue TabLayout and ViewPager together
        pager.adapter = PageAdapter(this);
//        // Design purpose. Tabs have the same width
        tabs.tabMode = TabLayout.MODE_FIXED

        TabLayoutMediator(
            tabs, pager
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> tab.text = getString(R.string.trouves)
                1 -> tab.text = getString(R.string.perdus)
            }
        }.attach()
    }

    //for signout firebase
    private fun signOutUserFromFirebase() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK))
    }

    //for delete account to firebase
    private fun onClickDelete() {
        MaterialAlertDialogBuilder(this)
            .setMessage(resources.getString(R.string.suppCompte))
            .setNegativeButton(resources.getString(R.string.non)) { dialog, wich ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.oui)) { dialog, wich ->
                deleteUserFromFirebase()
                Snackbar.make(mainPageBinding.root, getString(R.string.suppCpte), Snackbar.LENGTH_SHORT).show()
            }
            .show()
    }

    //for retrieve all announcement for user
    private fun retrieveAnnouncementForUser() {

        val user = userObject.uid
       val userAnnouncement = user == userUid
        if(userAnnouncement) {
            anouncementList.addAll(mutableListOf(userAnnouncement.toString()))
            Log.d("userAnnouncement", "userAnnouncement$anouncementList")
        }

    }

    //for delete user
    private fun deleteUserFromFirebase() {
        if (getCurrentUser() != null) {
            AuthUI.getInstance()
                .delete(this)
                .addOnSuccessListener(this, updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK))
        }
    }

    private fun updateUIAfterRESTRequestsCompleted(origin: Int): OnSuccessListener<Void?> {
        return OnSuccessListener {
            when (origin) {
                SIGN_OUT_TASK -> finish()
                DELETE_USER_TASK -> finish()
                else -> {
                }
            }
        }
    }

    private fun updateUINavHeader() {
        if (getCurrentUser() != null) {
            val navigationView: NavigationView = mainPageBinding.mainPageNavView
            val headerView: View = navigationView.getHeaderView(0) //For return layout
            val mPhotoHeader: ImageView = headerView.findViewById(R.id.photo_header)
            val mNameHeader = headerView.findViewById<TextView>(R.id.name_header)
            val mMailHeader = headerView.findViewById<TextView>(R.id.mail_header)
            // get photo in Firebase
            if (getCurrentUser()?.photoUrl != null) {
                Glide.with(this)
                    .load(getCurrentUser()?.photoUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mPhotoHeader)
            } else {
                mPhotoHeader.setImageResource(R.drawable.no_image)
            }
            //Get email
            val email = if (TextUtils.isEmpty(
                    getCurrentUser()?.email
                )
            ) "No Email Found" else getCurrentUser()?.email
            //Get Name
            val name = if (TextUtils.isEmpty(
                    getCurrentUser()?.displayName
                )
            ) "No Username Found" else getCurrentUser()?.displayName
            //Update With data
            mNameHeader.text = name
            mMailHeader.text = email
        }
    }
}



