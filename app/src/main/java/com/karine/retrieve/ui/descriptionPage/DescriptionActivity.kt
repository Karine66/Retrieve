package com.karine.retrieve.ui.descriptionPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karine.retrieve.databinding.ActivityDescriptionBinding

class DescriptionActivity : AppCompatActivity() {

    private lateinit var descriptionBinding: ActivityDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        descriptionBinding = ActivityDescriptionBinding.inflate(layoutInflater)
        val view = descriptionBinding.root
        setContentView(view)
    }
}