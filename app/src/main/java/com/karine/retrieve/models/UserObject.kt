package com.karine.retrieve.models
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp


class UserObject(
    var docId : String,
    val uid: String,
    var created: Timestamp?,
    var pseudo: String?,
    var email: String?,
    var phone: String?,
    var date: String?,
    var type: String?,
    var address: String?,
    var postalCode: Int?,
    var city: String?,
    var description: String?,
    var photo: MutableList<String>

                 ): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList() as MutableList<String>
   )

    constructor() : this("","",null,"","","","","","",null,"","", mutableListOf())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(docId)
        parcel.writeString(uid)
        parcel.writeParcelable(created, flags)
        parcel.writeString(pseudo)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(date)
        parcel.writeString(type)
        parcel.writeString(address)
        parcel.writeValue(postalCode)
        parcel.writeString(city)
        parcel.writeString(description)
        parcel.writeStringList(photo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserObject> {
        override fun createFromParcel(parcel: Parcel): UserObject {
            return UserObject(parcel)
        }

        override fun newArray(size: Int): Array<UserObject?> {
            return arrayOfNulls(size)
        }
    }
}






