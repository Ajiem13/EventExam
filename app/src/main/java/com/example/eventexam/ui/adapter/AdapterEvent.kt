package com.example.eventexam.ui.adapter



import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventexam.data.database.EventEntity
import com.example.eventexam.data.remote.response.ListEventsItem
import com.example.eventexam.databinding.ItemEventBindingBinding
import com.example.eventexam.ui.DetailEventActivity

class AdapterEvent(private var events: List<ListEventsItem>) : RecyclerView.Adapter<AdapterEvent.EventViewHolder>() {

    inner class EventViewHolder(val binding: ItemEventBindingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvEventTitle.text = event.name
            binding.tvEventDate.text = event.beginTime
            Glide.with(itemView.context)
                .load(event.imageLogo)
                .into(binding.imgEvent)
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailEventActivity::class.java).apply {
                    putExtra("EXTRA_DATA", event)
                    Log.d("Adapter", "Event yang diklik: ID=${event.id}, Name=${event.name}")

                }
                binding.root.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBindingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])


    }

    override fun getItemCount(): Int = events.size

    fun updateList(newEvents: List<ListEventsItem>) {
        events = newEvents
        notifyDataSetChanged()
    }
}
