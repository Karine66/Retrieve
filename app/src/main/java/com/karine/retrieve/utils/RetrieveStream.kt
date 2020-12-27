package com.karine.retrieve.utils

import com.karine.retrieve.models.geocoding.Response
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit


class RetrieveStream {

//    fun streamFetchGeocode(address: String): Observable<Response> {
//        val retrieveService: RetrieveService =
//            RetrieveRetrofitObject.retrofit.create(
//                RetrieveService::class.java
//            )
//        return retrieveService.getGeocode(address)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .timeout(10, TimeUnit.SECONDS)
//    }
}