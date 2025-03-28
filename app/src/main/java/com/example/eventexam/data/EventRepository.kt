package com.example.eventexam.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.eventexam.data.database.EventDao
import com.example.eventexam.data.database.EventEntity
import com.example.eventexam.data.remote.response.ListEventsItem
import com.example.eventexam.data.remote.retorfit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(private val apiService: ApiService, private val eventDao: EventDao) {

    suspend fun getUpcomingEvents(): List<ListEventsItem> {
        Log.d("API_RESPONSE", "Data dari API: ${apiService.getEvents(1)}")
        return apiService.getEvents(1).listEvents
    }

    suspend fun getFinishedEvents(): List<ListEventsItem> {
        return apiService.getEvents(0).listEvents
    }

    suspend fun getNearestEvent(): ListEventsItem? {
        return try {
            val response = apiService.getNearestEvent(active = -1, limit = 1)
            response.listEvents.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

   fun getFavoriteEvents(): LiveData<List<EventEntity>> {
        return eventDao.getFavoriteEvents()
    }

    suspend fun addToFavorites(event: EventEntity?) {
        if (event == null) {
            Log.e("EventRepository", "Event NULL! Tidak bisa ditambahkan ke favorit.")
            return
        }

        Log.d("EventRepository", "Menambahkan event ke favorit: id=${event.id}, name=${event.name}")

        val safeEvent = EventEntity(
            id = event.id,
            name = event.name,
            description = event.description,
            summary = event.summary,
            imageLogo = event.imageLogo
        )

        eventDao.insertEvent(safeEvent)

        val allFavorites = getFavoriteEvents()
        Log.d("EventRepositoryXXX", "Data favorit setelah insert: $allFavorites")
    }

    suspend fun removeFromFavorites(event: EventEntity) {
        eventDao.deleteEvent(event)
    }

    suspend fun isEventFavorite(eventId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val event = eventDao.getEventById(eventId)
            Log.d("EventRepository", "Checking favorite for event ID: $eventId, Found: ${event != null}")
            event != null
        }
    }

}
