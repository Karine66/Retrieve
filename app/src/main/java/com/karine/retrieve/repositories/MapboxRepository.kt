package com.karine.retrieve.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.karine.retrieve.R
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Point
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapboxRepository {

//    private lateinit var firstResultPoint: Point
//   var firstResultPoint : MutableLiveData<Point> = MutableLiveData()
//
//    fun getGeocodingMapbox(completeAddress:String): MutableLiveData<Point> {
//
//        val mapBoxGeocoding = MapboxGeocoding.builder()
//            .accessToken(R.string.access_token.toString())
//            .query(completeAddress)
//            .build()
//
//        mapBoxGeocoding.enqueueCall(object : Callback<GeocodingResponse> {
//            override fun onResponse(
//                call: Call<GeocodingResponse>,
//                response: Response<GeocodingResponse>
//            ) {
//
//                val results = response.body()!!.features()
//
//                if (results.size > 0) {
//
//                    // Log the first results Point.
//                    firstResultPoint = results[0].center()!!
//                    Log.d("onResponse", "onResponse: $firstResultPoint")
//                    firstResultPoint.value
//
//
//                } else {
//
//                    Log.d("OnNoResponse", "onResponse: No result found")
//
//                }
//            }
//
//            override fun onFailure(call: Call<GeocodingResponse>, throwable: Throwable) {
//                throwable.printStackTrace()
//            }
//        })
//        return firstResultPoint
//    }
}