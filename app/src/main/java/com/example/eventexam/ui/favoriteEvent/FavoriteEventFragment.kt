package com.example.eventexam.ui.favoriteEvent

import com.example.eventexam.ui.EventViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventexam.databinding.FragmentFavoriteBinding
import com.example.eventexam.ui.DetailEventActivity

class FavoriteEventFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: EventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AdapterEventFavorite { event ->
            val intent = Intent(requireContext(), DetailEventActivity::class.java)
            intent.putExtra("EXTRA_DATA", event)
            startActivity(intent)
        }

        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotification.adapter = adapter


        viewModel.favoriteEvents.observe(viewLifecycleOwner) { favorites ->
            if (favorites == null) {
                Log.e("FavoriteFragment", "Error: Data favorit masih NULL!")
            } else {
                Log.d("FavoriteFragment", "Data Favorit: $favorites")
                adapter.submitList(favorites)
            }
        }

    }
}

