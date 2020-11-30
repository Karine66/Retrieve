package com.karine.retrieve.ui.mainPage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class PageAdapter(fa: FragmentActivity) : FragmentStateAdapter(
    fa

){




//    override fun getCount(): Int = 2
//
//    override fun getItem(position: Int): Fragment {
//
//        return when (position) {
//            0 -> {
//                FindLostFragment()
//            }
//
//            1 -> {
//                FindLostFragment()
//            }
//            else ->  {throw IllegalStateException("$position is illegal") }
//        }
//    }
//
//   override fun getPageTitle(position: Int): CharSequence? {
//        return when (position) {
////           0 -> context.getString(R.string.trouves)
//            0 -> "Objets trouvés"
//            1 -> "Objets perdus"
//            else -> null
//        }
//    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                FindLostFragment()
            }

            1 -> {
                FindLostFragment()
            }
            else ->  {throw IllegalStateException("$position is illegal") }
        }
    }
}


