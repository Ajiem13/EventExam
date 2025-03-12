package com.example.eventexam.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.eventexam.data.EventRepository
import com.example.eventexam.data.database.EventDao
import com.example.eventexam.data.database.EventDatabase
import com.example.eventexam.data.database.EventEntity
import com.example.eventexam.data.remote.response.ListEventsItem
import com.example.eventexam.data.remote.retorfit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val eventDao: EventDao = EventDatabase.getDatabase(application).eventDao()
    private val repository = EventRepository(ApiConfig.getApiService(), eventDao)

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> get() = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> get() = _finishedEvents

    private val _selectedEvent = MutableLiveData<EventEntity?>()
    val selectedEvent: LiveData<EventEntity?> get() = _selectedEvent

    lateinit var favoriteEvents: LiveData<List<EventEntity>>


    init {
        viewModelScope.launch {
            loadUpcomingEvents()
            loadFinishedEvents()
            favoriteEvents = repository.getFavoriteEvents()
        }

    }

    fun loadUpcomingEvents() {
        viewModelScope.launch {
            try {
                val events = repository.getUpcomingEvents()
                Log.d("API_RESPONSE", "Data dari API: $events")
                _upcomingEvents.postValue(events)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Gagal mengambil data", e)
            }
        }
    }


    fun loadFinishedEvents() {
        viewModelScope.launch {
            try {
                val events = repository.getFinishedEvents()
                Log.d("VIEWMODEL_DATA", "Data diterima di ViewModel: $events")
                _finishedEvents.postValue(events)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getEventDetail(id: Int) {
        viewModelScope.launch {
            val event = repository.getEventDetail(id)
            Log.d("EventViewModelgetDetail", "Event dari repository: $event")
            _selectedEvent.value = event
        }
    }


    fun addToFavorites(event: EventEntity?) {
        viewModelScope.launch {
            if (event != null) {
                repository.addToFavorites(event)
            } else {
                Log.e("com.example.eventexam.ui.EventViewModel", "Event NULL! Tidak bisa ditambahkan ke favorit.")
            }
        }
    }


    fun removeFromFavorites(event: EventEntity) {
        viewModelScope.launch {
            repository.removeFromFavorites(event)
        }
    }

    fun isEventFavorite(eventId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.isEventFavorite(eventId)
                withContext(Dispatchers.Main) { // ✅ Kembali ke Main Thread untuk UI
                    Log.d("EventViewModel", "isEventFavorite() -> Event ID: $eventId, Result: $result")
                    callback(result)
                }
            } catch (e: Exception) {
                Log.e("EventViewModel", "Error checking favorite: ${e.message}")
            }
        }
    }





}
