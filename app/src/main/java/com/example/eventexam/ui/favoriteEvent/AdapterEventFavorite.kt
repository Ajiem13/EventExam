package com.example.eventexam.ui.favoriteEvent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventexam.data.database.EventEntity
import com.example.eventexam.databinding.ItemEventBindingBinding

class AdapterEventFavorite(private val onClick: (EventEntity) -> Unit) :
    ListAdapter<EventEntity, AdapterEventFavorite.EventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBindingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onClick)
    }

    class EventViewHolder(private val binding: ItemEventBindingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity, onClick: (EventEntity) -> Unit) {
            binding.tvEventTitle.text = event.name
            binding.tvDescripiton.text = event.summary
            Glide.with(binding.root.context).load(event.imageLogo).into(binding.imgEvent)

            binding.root.setOnClickListener { onClick(event) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
