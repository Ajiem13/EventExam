package com.example.eventexam.data.remote.retorfit

import com.example.eventexam.data.remote.response.ResponseEvent
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

        @GET("events")
        suspend fun getEvents(@Query("active") active: Int): ResponseEvent

        @GET("events")
        suspend fun getNearestEvent(@Query("active") active: Int, @Query("limit") limit : Int): ResponseEvent

}
