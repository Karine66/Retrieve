package com.karine.retrieve.ui.listPage


import androidx.recyclerview.widget.RecyclerView
import com.karine.retrieve.databinding.FragmentListItemBinding
import com.karine.retrieve.models.UserObject

class ListViewHolder(private val fragmentListItemBinding: FragmentListItemBinding) : RecyclerView.ViewHolder(fragmentListItemBinding.root) {



  fun updateWithObject( userObject: UserObject) {

      fragmentListItemBinding.typeObject.text = userObject.type
      fragmentListItemBinding.City.text = userObject.city
      fragmentListItemBinding.name.text = userObject.pseudo
  }

}