package com.karine.retrieve.ui.listPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.karine.retrieve.R
import com.karine.retrieve.databinding.FragmentListBinding


class ListFragment : Fragment() {

    //For view binding
    private var listBinding : FragmentListBinding? = null
    private val binding get() = listBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //for View Binding
        listBinding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listBinding = null
    }
}