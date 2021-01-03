package com.karine.retrieve.ui.listPage


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.karine.retrieve.R
import com.karine.retrieve.databinding.FragmentListBinding
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.ui.UserObjectViewModel
import com.karine.retrieve.ui.descriptionPage.DescriptionActivity
import com.karine.retrieve.utils.CellClickListener



class ListFragment : Fragment(), CellClickListener {

    //For view binding
    private var listBinding : FragmentListBinding? = null
    private val binding get() = listBinding!!

    private lateinit var listAdapter : ListAdapter
    private lateinit var userObjectViewModel: UserObjectViewModel
    private var objectFind:Boolean = true

    private var firestoreDB = FirebaseFirestore.getInstance()
    private val objectRef = firestoreDB.collection("usersObjectFind")
    private val objectRefLost = firestoreDB.collection("usersObjectLost")

    companion object {
        fun newInstance(objectFind: Boolean) : ListFragment {
            val bundle = Bundle()
            bundle.putBoolean("key", objectFind)
            val fragment = ListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            objectFind = it.getBoolean("key", true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //for View Binding
        listBinding = FragmentListBinding.inflate(inflater, container, false)

        this.setUpRecyclerView()
        this.configureViewModel()
        return binding.root
    }


    private fun setUpRecyclerView() {

        val query: Query = if(objectFind) {
            objectRef.orderBy("created", Query.Direction.DESCENDING)
        } else {
            objectRefLost.orderBy("created", Query.Direction.DESCENDING)
        }
        val options = FirestoreRecyclerOptions.Builder<UserObject>()
            .setQuery(query, UserObject::class.java)
            .build()
        listAdapter = ListAdapter(options, Glide.with(this), this)
        val recyclerView: RecyclerView = listBinding!!.fragmentListRV
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = listAdapter
    }
    override fun onStart() {
        super.onStart()
        listAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        listAdapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listBinding = null
    }

    private fun configureViewModel() {
        userObjectViewModel = ViewModelProvider(this).get(UserObjectViewModel::class.java)

    }

    //for click on item recycler view
    override fun onCellClickListener(userObject: UserObject) {

        val intent = Intent(context, DescriptionActivity::class.java)
        intent.putExtra("userObject", userObject)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu( menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val item = menu.findItem(R.id.actionSearch)
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                return true
            }
        })
    }
}

