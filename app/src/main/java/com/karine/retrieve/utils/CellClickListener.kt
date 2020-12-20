package com.karine.retrieve.utils

import com.karine.retrieve.models.UserObject

interface CellClickListener {
    //for item click recycler view
    fun onCellClickListener(userObject: UserObject, position: Int)
}