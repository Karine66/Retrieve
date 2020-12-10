package com.karine.retrieve.ui.listPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karine.retrieve.databinding.FragmentListBinding
import com.karine.retrieve.models.UserObject


class ListFragment : Fragment() {

    //For view binding
    private var listBinding : FragmentListBinding? = null
    private val binding get() = listBinding!!

    private lateinit var listObject: MutableList<UserObject>
    private lateinit var listAdapter : ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //for View Binding
        listBinding = FragmentListBinding.inflate(inflater, container, false)

        this.configureRecyclerView()
        return binding.root


    }

    private fun configureRecyclerView() {
        this.listObject = mutableListOf()
        //Create adapter
        this.listAdapter = ListAdapter(this.listObject)
        //Set Layout manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        listBinding?.fragmentListRV?.layoutManager = layoutManager
        listBinding?.fragmentListRV?.adapter = listAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listBinding = null
    }

}