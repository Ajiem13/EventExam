package com.example.eventexam.data.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.eventexam.data.remote.response.ListEventsItem

@Dao
interface EventDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(event:EventEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAll(eve)

//
//
//
//        @Insert(onConflict = OnConflictStrategy.REPLACE)
//        suspend fun insertFavorite(event: EventEntity)
//
//        @Delete
//        suspend fun deleteFavorite(event: EventEntity)
//
//        @Query("SELECT * FROM events")
//        fun getAllFavorites(): List<EventEntity>


        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertEvent(event: EventEntity)

        @Query("SELECT * FROM events")
        fun getFavoriteEvents(): LiveData<List<EventEntity>>

        @Query("SELECT * FROM events WHERE id = :eventId")
        fun getEventById(eventId: Int): EventEntity

        @Delete
        suspend fun deleteEvent(event:EventEntity)



}

