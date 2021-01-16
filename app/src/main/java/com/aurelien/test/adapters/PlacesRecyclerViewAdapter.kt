package com.aurelien.test.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aurelien.test.R
import com.aurelien.test.databinding.PlaceItemBinding
import com.aurelien.test.services.models.Place

class PlacesRecyclerViewAdapter(
    private var places: List<Place>,
    private val callbacks: PlacesRecyclerViewAdapterCallback
) : RecyclerView.Adapter<PlaceItemViewHolder>(),
    PlaceItemViewHolder.PlaceItemItemViewHolderCallback {

    interface PlacesRecyclerViewAdapterCallback {
        fun placeClicked(place: Place)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaceItemViewHolder(
            PlaceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            this
        )

    override fun onBindViewHolder(holder: PlaceItemViewHolder, position: Int) {
        holder.bind(places[position])
    }

    fun setPlaces(places: List<Place>) {
        this.places = places
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = places.size

    override fun placeClicked(place: Place) {
        places.find { it.id == place.id }
            ?.isFavorite = place.isFavorite
        callbacks.placeClicked(place)
    }
}

class PlaceItemViewHolder(
    private val binding: PlaceItemBinding,
    private val callbacks: PlaceItemItemViewHolderCallback
) : RecyclerView.ViewHolder(binding.root) {

    interface PlaceItemItemViewHolderCallback {
        fun placeClicked(place: Place)
    }

    fun bind(place: Place) {
        binding.name.text = place.name
        binding.favoriteIcon.setImageResource(getFavoriteIcon(place.isFavorite))

        binding.layout.setOnClickListener {
            place.isFavorite = !place.isFavorite
            binding.favoriteIcon.setImageResource(getFavoriteIcon(place.isFavorite))
            callbacks.placeClicked(place)
        }
    }

    private fun getFavoriteIcon(isFavorite: Boolean): Int {
        return if (isFavorite) {
            R.drawable.ic_favorite_selected
        } else {
            R.drawable.ic_favorite
        }
    }
}