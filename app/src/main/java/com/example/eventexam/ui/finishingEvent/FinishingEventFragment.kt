package com.example.eventexam.ui.finishingEvent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventexam.data.remote.response.ListEventsItem
import com.example.eventexam.databinding.FragmentFinishingEventBinding
import com.example.eventexam.ui.EventViewModel
import com.example.eventexam.ui.adapter.AdapterEvent

class FinishingEventFragment : Fragment() {
    private lateinit var binding : FragmentFinishingEventBinding
    private val viewModel: EventViewModel by viewModels()
    private lateinit var eventAdapter: AdapterEvent
    private var originalList = listOf<ListEventsItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFinishingEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = AdapterEvent(emptyList())

        binding.rvFinisihingEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = eventAdapter
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            originalList = events
            eventAdapter.updateList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterEvents(newText)
                return true
            }
        })
    }

    private fun filterEvents(query: String?) {
        val filteredList = if (!query.isNullOrEmpty()) {
            originalList.filter { it.name.contains(query, true) }
        } else {
            originalList
        }
        eventAdapter.updateList(filteredList)
    }
}
