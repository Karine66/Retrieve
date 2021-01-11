package com.karine.retrieve.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karine.retrieve.repositories.MapboxRepository
import com.mapbox.geojson.Point

class MapBoxViewModel : ViewModel() {

  var mapboxRepository = MapboxRepository()

    fun geocodingPoint(completeAddress:String): MutableLiveData<Point> {
      return mapboxRepository.getGeocodingMapbox(completeAddress)

    }
}