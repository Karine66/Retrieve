package com.karine.retrieve.models

class UserObject (val uid : String,
                  var pseudo : String?,
                  var email : String?,
                  var phone : Int?,
                  var date : Long?,
                  var type : String?,
                  var address : String?,
                  var postalCode : Int?,
                  var city : String?,
                  var description : String?,


                  ){
    constructor() : this("","","",null,null,"","",null,"","")


}
