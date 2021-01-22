package com.karine.retrieve.ui.listPage


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.karine.retrieve.R
import com.karine.retrieve.databinding.FragmentListBinding
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.ui.descriptionPage.DescriptionActivity
import com.karine.retrieve.utils.CellClickListener


class ListFragment : Fragment(), CellClickListener {

    //For view binding
    private var listBinding: FragmentListBinding? = null
    private val binding get() = listBinding!!

    private lateinit var listAdapter: ListAdapter
    private lateinit var type: String
    private var objectFind: Boolean = true

    private var firestoreDB = FirebaseFirestore.getInstance()
    private val objectRef = firestoreDB.collection("usersObjectFind")
    private val objectRefLost = firestoreDB.collection("usersObjectLost")

    companion object {
        fun newInstance(objectFind: Boolean): ListFragment {
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
        this.dropDownAdapter()
        this.listSearch()
        this.clickCancelSearch()
        //for SearchView
        setHasOptionsMenu(true);
        return binding.root
    }

    /**
     * For set up RecyclerView
     */
    private fun setUpRecyclerView() {

        val query: Query = if (objectFind) {
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

    /**
     * For click on item recyclerView
     */
    override fun onCellClickListener(userObject: UserObject) {
        val intent = Intent(context, DescriptionActivity::class.java)
        intent.putExtra("userObject", userObject)
        intent.putExtra("objectFind", objectFind)
        startActivity(intent)
    }

    /**
     * For dropdown
     */
    private fun factoryAdapter(resId: Int): ArrayAdapter<String?> {
        return ArrayAdapter(
            context!!,
            R.layout.dropdown_menu_popup_item,
            resources.getStringArray(resId)
        )
    }

    /**
     * For dropdown
     */
    private fun dropDownAdapter() {
        binding.etType.setAdapter(factoryAdapter(R.array.Type))
    }

    /**
     * For search with dropdown
     */
    private fun listSearch() {
        binding.etType.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {

                val value = binding.etType.text.toString()
                val firebaseSearchQuery: Query = if (objectFind) {
                    objectRef.whereEqualTo("type", value)
                } else {
                    objectRefLost.whereEqualTo("type", value)
                }
                //set Options
                val options = FirestoreRecyclerOptions.Builder<UserObject>()
                    .setQuery(firebaseSearchQuery, UserObject::class.java)
                    .build()

                listAdapter.updateOptions(options)
            }
        }
    }

    /**
     * For cancel search
     */
    private fun clickCancelSearch() {
        binding.buttonCancel.setOnClickListener(View.OnClickListener {
            binding.etType.setText("")
            val query: Query = if (objectFind) {
                objectRef.orderBy("created", Query.Direction.DESCENDING)
            } else {
                objectRefLost.orderBy("created", Query.Direction.DESCENDING)
            }
            val options = FirestoreRecyclerOptions.Builder<UserObject>()
                .setQuery(query, UserObject::class.java)
                .build()
            listAdapter.updateOptions(options)
        })
    }
}

