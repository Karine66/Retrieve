package com.karine.retrieve.ui.mainPage

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityMainPageBinding


class MainPageActivity : AppCompatActivity() {

    private lateinit var mainPageBinding: ActivityMainPageBinding
    private var ab: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainPageBinding = ActivityMainPageBinding.inflate(layoutInflater)
        val view = mainPageBinding.root
        setContentView(view)

        configureToolbar()
    }
    //for toolbar
    private fun configureToolbar() {
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }


}