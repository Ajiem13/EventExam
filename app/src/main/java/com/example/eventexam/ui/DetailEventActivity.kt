package com.example.eventexam.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eventexam.R
import com.example.eventexam.data.database.EventEntity
import com.example.eventexam.data.remote.response.ListEventsItem
import com.example.eventexam.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding
    private var isFavorite : Boolean = false
    private val viewModel: EventViewModel by viewModels()
    private var eventData: ListEventsItem? = null
    private var eventId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val data = intent.getParcelableExtra<Parcelable>("EXTRA_DATA")

        if (data != null) {
            when(data){
                is EventEntity -> {
                    eventId = data.id
                    Glide.with(this).load(data.imageLogo).into(binding.imgEvent)
                    binding.tvEventTitle.text = data.name
                    binding.tvEventDescription.text = convertHtmlToText(data.description)
                    binding.tvEventDateLocation.text = data.summary
                }
                is ListEventsItem -> {
                    eventData = data
                    eventId = data.id
                    Glide.with(this).load(data.imageLogo).into(binding.imgEvent)
                    binding.tvEventTitle.text = data.name
                    binding.tvEventDescription.text = convertHtmlToText(data.description)
                    binding.tvEventDateLocation.text = data.summary
                }
                else -> {}
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventData?.link))
            startActivity(intent)
        }

        binding.btnFavorite.setOnClickListener {
            if (isFavorite) {
                removeFromFavorites()
            } else {
                addToFavorites()
            }
        }

        viewModel.isEventFavorite(eventId) { isFav ->
            isFavorite = isFav
            Log.d("DetailEventActivity", "Event ID: $eventId, Favorite: $isFav") // 🔍 Debugging Log
            changeFavoriteIcon(isFavorite)
        }


    }

    private fun convertHtmlToText(html: String): String {
        return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun removeFromFavorites() {
        val event = EventEntity(
            eventId,
            binding.tvEventTitle.text.toString(),
            binding.tvEventDescription.text.toString(),
            eventData?.summary ?: "",
            eventData?.imageLogo ?: ""
        )

        viewModel.removeFromFavorites(event)
        isFavorite = false
        changeFavoriteIcon(isFavorite)
        Toast.makeText(this, "Menghapus dari favorite", Toast.LENGTH_SHORT).show()
    }

    private fun addToFavorites() {
        val event = EventEntity(
            eventId,
            binding.tvEventTitle.text.toString(),
            binding.tvEventDescription.text.toString(),
            eventData?.summary ?: "",
            eventData?.imageLogo ?: ""
        )

        viewModel.addToFavorites(event)
        isFavorite = true
        changeFavoriteIcon(isFavorite)
        Toast.makeText(this, "Menambahkan ke favorite", Toast.LENGTH_SHORT).show()
    }

    private fun changeFavoriteIcon(isFavorited: Boolean) {
        if (isFavorited) {
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite_filled)
        } else{
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite)
        }

    }
}
