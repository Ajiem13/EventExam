package com.example.eventexam.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertEvent(event: EventEntity)

        @Query("SELECT * FROM events")
        fun getFavoriteEvents(): LiveData<List<EventEntity>>

        @Query("SELECT * FROM events WHERE id = :eventId")
        fun getEventById(eventId: Int): EventEntity

        @Delete
        suspend fun deleteEvent(event:EventEntity)



}

