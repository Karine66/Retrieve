package com.karine.retrieve.ui.listPage


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.karine.retrieve.R
import com.karine.retrieve.databinding.FragmentListItemBinding
import com.karine.retrieve.models.UserObject

class ListViewHolder(private val fragmentListItemBinding: FragmentListItemBinding) : RecyclerView.ViewHolder(fragmentListItemBinding.root) {



  fun updateWithObject( userObject: UserObject) {

      fragmentListItemBinding.typeObject.text = userObject.type
      fragmentListItemBinding.City.text = userObject.city

//      if (userObject.photo[0].isNotEmpty()) {
//          glide.load(userObject.photo[0]).apply(RequestOptions.centerCropTransform()).into(fragmentListItemBinding.listPhoto);
//      } else {
//         fragmentListItemBinding.listPhoto.setImageResource(R.drawable.no_image);
//      }
  }
  }

