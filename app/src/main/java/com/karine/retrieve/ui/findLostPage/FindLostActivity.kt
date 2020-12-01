package com.karine.retrieve.ui.findLostPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.karine.retrieve.databinding.ActivityFindLostBinding


class FindLostActivity : AppCompatActivity() {

    private lateinit var findLostBinding: ActivityFindLostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findLostBinding = ActivityFindLostBinding.inflate(layoutInflater)
        val view = findLostBinding.root
        setContentView(view)


    }


}