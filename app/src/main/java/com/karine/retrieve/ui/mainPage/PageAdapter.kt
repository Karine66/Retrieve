package com.karine.retrieve.ui.mainPage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.karine.retrieve.ui.listPage.ListFragment


class PageAdapter(fa: FragmentActivity) : FragmentStateAdapter(
    fa

){
    /**
     * for number tabs
     */
    override fun getItemCount(): Int {
        return 2
    }

    /**
     * For fragments attached to tabs
     */
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ListFragment.newInstance(true)
            }
            1 -> {
                ListFragment.newInstance(false)
            }
            else ->  {throw IllegalStateException("$position is illegal") }
        }
    }


}


