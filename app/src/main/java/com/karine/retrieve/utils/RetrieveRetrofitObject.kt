package com.karine.retrieve.utils

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class RetrieveRetrofitObject {

    companion object {
        var retrofit = Retrofit.Builder() //define root URL
            .baseUrl("https://api.mapbox.com/") //serialize Gson
            .addConverterFactory(GsonConverterFactory.create()) //For RxJava
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}