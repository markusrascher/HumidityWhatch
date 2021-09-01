package com.faramiki.humiditywhatch.network

import com.faramiki.humiditywhatch.entities.Fals22DataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitFals22Interface {

    @GET("/app/tag.php")
    fun getDataPoints(
        @Query("app_id") appId:String,
        @Query("date") date:String
    ) : Call<List<Fals22DataModel>>
}