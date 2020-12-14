package com.karine.retrieve.models

import android.net.Uri
import com.google.firebase.Timestamp
import java.io.Serializable

class UserObject(
    val uid: String,
    var created: Timestamp?,
    var pseudo: String?,
    var email: String?,
    var phone: Int?,
    var date: String?,
    var type: String?,
    var address: String?,
    var postalCode: Int?,
    var city: String?,
    var description: String?,
    var photo: MutableList<String>


                  ) : Serializable {
    constructor() : this("",null,"","",null,"","","",null,"","", mutableListOf())


}
