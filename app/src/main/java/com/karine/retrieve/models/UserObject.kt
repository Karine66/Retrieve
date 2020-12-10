package com.karine.retrieve.models

import android.net.Uri

class UserObject (val uid : String,
                  var pseudo : String?,
                  var email : String?,
                  var phone : Int?,
                  var date : String?,
                  var type : String?,
                  var address : String?,
                  var postalCode : Int?,
                  var city : String?,
                  var description : String?,
                  var photo :MutableList<String>


                  ){
    constructor() : this("","","",null,"","","",null,"","", mutableListOf())


}
