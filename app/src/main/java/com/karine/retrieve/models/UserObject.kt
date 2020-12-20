package com.karine.retrieve.models
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*



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

){
//                 ): Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString().toString(),
//        parcel.readParcelable(Timestamp::class.java.classLoader),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readString(),
//        parcel.readString(),
//        parcel.createStringArrayList() as MutableList<String>
//    )
//    {
//    }

    constructor() : this("",null,"","",null,"","","",null,"","", mutableListOf())
//
//    override fun describeContents(): Int {
//       return 0
//    }
//
//    override fun writeToParcel(dest: Parcel?, flags: Int) {
//        dest!!.writeString(uid)
//        dest.writeValue(created)
//        dest.writeString(pseudo)
//        dest.writeString(email)
//        dest.writeInt(phone!!)
//        dest.writeString(date)
//        dest.writeString(type)
//        dest.writeString(address)
//        dest.writeInt(postalCode!!)
//        dest.writeString(city)
//        dest.writeString(description)
//        dest.writeStringList(photo)
//    }
//
//    companion object CREATOR : Parcelable.Creator<UserObject> {
//        override fun createFromParcel(parcel: Parcel): UserObject {
//            return UserObject(parcel)
//        }
//
//        override fun newArray(size: Int): Array<UserObject?> {
//            return arrayOfNulls(size)
//        }
//    }


}




