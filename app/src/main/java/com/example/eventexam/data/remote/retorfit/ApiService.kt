package com.example.eventexam.data.remote.retorfit

import com.example.eventexam.data.database.EventEntity
import com.example.eventexam.data.remote.response.ListEventsItem
import com.example.eventexam.data.remote.response.ResponseEvent
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

        @GET("events")
        suspend fun getEvents(@Query("active") active: Int): ResponseEvent

        @GET("events/{id}")
        suspend fun getEventDetail(@Path("id") id: Int): EventEntity

}
