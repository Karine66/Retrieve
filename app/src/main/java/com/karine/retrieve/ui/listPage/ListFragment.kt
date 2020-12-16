package com.karine.retrieve.ui.listPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.karine.retrieve.databinding.FragmentListBinding
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.ui.descriptionPage.DescriptionActivity
import com.karine.retrieve.utils.CellClickListener
import com.karine.retrieve.utils.GlideApp
import com.karine.retrieve.utils.Utils


class ListFragment : Fragment(), CellClickListener {



    //For view binding
    private var listBinding : FragmentListBinding? = null
    private val binding get() = listBinding!!

    private lateinit var listObject: MutableList<UserObject>
    private lateinit var listAdapter : ListAdapter
    private var findTabClick: Int = 0


    var firestoreDB = FirebaseFirestore.getInstance()
    val objectRef = firestoreDB.collection("usersObjectFind")
    val objectRefLost = firestoreDB.collection("userObjectLost")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //for View Binding
        listBinding = FragmentListBinding.inflate(inflater, container, false)

        this.setUpRecyclerView()

        return binding.root


    }

    private fun setUpRecyclerView() {

        val query: Query = objectRef.orderBy("created", Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions.Builder<UserObject>()
            .setQuery(query, UserObject::class.java)
            .build()
        listAdapter = ListAdapter(options, GlideApp.with(this), this)
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
    //for click on item recycler view
    override fun onCellClickListener() {
        val intent = Intent(context, DescriptionActivity::class.java)
        startActivity(intent)

        Toast.makeText(context, "Cell clicked", Toast.LENGTH_SHORT).show()
    }

}