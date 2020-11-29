package com.karine.retrieve.ui.mainPage

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.karine.retrieve.R


class PageAdapter(manager: FragmentManager) : FragmentPagerAdapter(
    manager,
    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
){




    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {

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

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
//            0 -> context.getString(R.string.trouves)
            0 -> "Objets trouvés"
            1 -> "Objets perdus"
            else -> null
        }
    }
    }


