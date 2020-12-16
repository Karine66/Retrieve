package com.karine.retrieve.ui.descriptionPage

import android.os.Bundle
import com.karine.retrieve.databinding.ActivityDescriptionBinding
import com.karine.retrieve.ui.BaseActivity

class DescriptionActivity : BaseActivity() {

    private lateinit var descriptionBinding: ActivityDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        descriptionBinding = ActivityDescriptionBinding.inflate(layoutInflater)
        val view = descriptionBinding.root
        setContentView(view)
    }

}