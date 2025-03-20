package com.example.eventexam.ui.upcomingEvent

import com.example.eventexam.ui.EventViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventexam.databinding.FragmentUpcomingEventBinding
import com.example.eventexam.ui.adapter.AdapterEvent

class UpcomingEventFragment : Fragment() {
    private lateinit var binding: FragmentUpcomingEventBinding
    private val viewModel: EventViewModel by viewModels()
    private lateinit var eventAdapter: AdapterEvent

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUpcomingEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = AdapterEvent(emptyList())

        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = eventAdapter
        }

        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            Log.d("VIEWMODEL_DATA", "Data diterima di Fragment: $events")
            eventAdapter.updateList(events)
            if (events.isEmpty()) {
                Toast.makeText(context, "Tidak ada event tersedia", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

    }
}

