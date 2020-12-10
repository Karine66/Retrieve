package com.karine.retrieve.ui.listPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karine.retrieve.databinding.FragmentListItemBinding
import com.karine.retrieve.models.UserObject


class ListAdapter(listObject: MutableList<UserObject>) : RecyclerView.Adapter<ListViewHolder>() {

    private val listObject : MutableList<UserObject> = mutableListOf()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            FragmentListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        );

    }



    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.updateWithObject(this.listObject[position])
    }

    override fun getItemCount(): Int {
        return this.listObject.size
    }
}