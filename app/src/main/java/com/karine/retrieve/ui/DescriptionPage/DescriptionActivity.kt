package com.karine.retrieve.ui.DescriptionPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karine.retrieve.databinding.ActivityDescriptionBinding
import com.karine.retrieve.databinding.ActivityLoginBinding

class DescriptionActivity : AppCompatActivity() {

    private lateinit var descriptionBinding: ActivityDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        descriptionBinding = ActivityDescriptionBinding.inflate(layoutInflater)
        val view = descriptionBinding.root
        setContentView(view)
    }
}