package com.faramiki.humiditywhatch.network

import com.faramiki.humiditywhatch.entities.Fals22DataModel
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitFals22Interface {

    @GET("/app/tag.php?app_id=10342&date=2021-08-26")
    fun getDataPoints() : Call<List<Fals22DataModel>>



}