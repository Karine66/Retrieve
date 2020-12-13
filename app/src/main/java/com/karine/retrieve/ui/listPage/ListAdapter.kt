package com.karine.retrieve.ui.listPage

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.karine.retrieve.databinding.FragmentListItemBinding
import com.karine.retrieve.models.UserObject



class ListAdapter(options: FirestoreRecyclerOptions<UserObject>, glide : RequestManager) : FirestoreRecyclerAdapter<UserObject, ListViewHolder>(
    options
) {

  private val glide : RequestManager? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val fragmentListItemBinding = FragmentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(fragmentListItemBinding)
    }


    override fun onBindViewHolder(
        listViewHolder: ListViewHolder,
        position: Int,
        userObject: UserObject
    ) {


        if (glide != null) {
            listViewHolder.updateWithObject(userObject, glide)
        }

    }


}