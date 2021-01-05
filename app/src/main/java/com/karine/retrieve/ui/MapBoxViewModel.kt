package com.karine.retrieve.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.repositories.MapboxRepository
import com.mapbox.api.geocoding.v5.models.GeocodingResponse

class MapBoxViewModel : ViewModel() {

    var mapboxRepository = MapboxRepository()
    var geocodingPoint: MutableLiveData<List<GeocodingResponse>> = MutableLiveData()

    fun geocodingPoint(completeAddress:String) {
      return mapboxRepository.getGeocodingMapbox(completeAddress)

    }
}