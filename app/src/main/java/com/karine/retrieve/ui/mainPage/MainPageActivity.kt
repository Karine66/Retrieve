package com.karine.retrieve.ui.mainPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.karine.retrieve.databinding.ActivityMainPageBinding

class MainPageActivity : AppCompatActivity() {

    private lateinit var mainPageBinding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainPageBinding = ActivityMainPageBinding.inflate(layoutInflater)
        val view = mainPageBinding.root
        setContentView(view)
    }
}