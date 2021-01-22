package com.karine.retrieve.ui.listPage


import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.karine.retrieve.R
import com.karine.retrieve.databinding.FragmentListItemBinding
import com.karine.retrieve.models.UserObject

class ListViewHolder(private val fragmentListItemBinding: FragmentListItemBinding) :
    RecyclerView.ViewHolder(fragmentListItemBinding.root) {

    fun updateWithObject(userObject: UserObject, glide: RequestManager) {

        fragmentListItemBinding.typeObject.text = userObject.type
        fragmentListItemBinding.City.text = userObject.city

        if (userObject.photo.isNotEmpty()) {
            glide.load(userObject.photo[0]).apply(RequestOptions.centerCropTransform())
                .into(fragmentListItemBinding.listPhoto);
            Log.d("glide", "glide" + userObject.photo[0])
        } else {
            fragmentListItemBinding.listPhoto.setImageResource(R.drawable.no_image);
        }
    }
}

