package com.karine.retrieve.ui.mainPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.karine.retrieve.databinding.FragmentFindLostBinding


class FindLostFragment : Fragment() {

    private var findLostBinding : FragmentFindLostBinding? = null
    private  val binding get() = findLostBinding!!

    fun newInstance(): FindLostFragment {
        return FindLostFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // For view binding
        findLostBinding = FragmentFindLostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        findLostBinding = null
    }
}