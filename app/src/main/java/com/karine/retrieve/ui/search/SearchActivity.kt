package com.karine.retrieve.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karine.retrieve.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var searchBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        val view = searchBinding.root
        setContentView(view)
    }
}