package com.karine.retrieve.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.karine.retrieve.R
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapboxRepository {


    fun getGeocodingMapbox(completeAddress:String): MutableLiveData<Point> {

   var firstResultPoint : MutableLiveData<Point> = MutableLiveData()

        val mapBoxGeocoding = MapboxGeocoding.builder()
            .accessToken(Mapbox.getAccessToken().toString())
            .query(completeAddress)
            .build()

        mapBoxGeocoding.enqueueCall(object : Callback<GeocodingResponse> {
            override fun onResponse(
                call: Call<GeocodingResponse>,
                response: Response<GeocodingResponse>
            ) {
                val results = response.body()!!.features()

                if (results.size > 0) {
                    // Log the result Point.
                    firstResultPoint.value = results[0].center()!!
                } else {

                    Log.d("OnNoResponse", "onResponse: No result found")
                }
            }

            override fun onFailure(call: Call<GeocodingResponse>, throwable: Throwable) {
                throwable.printStackTrace()
            }
        })
        return firstResultPoint
    }
}