package com.karine.retrieve.ui.mainPage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.karine.retrieve.ui.listPage.ListFragment


class PageAdapter(fa: FragmentActivity) : FragmentStateAdapter(
    fa

){

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ListFragment()
            }

            1 -> {
               ListFragment()
            }
            else ->  {throw IllegalStateException("$position is illegal") }
        }
    }
}


