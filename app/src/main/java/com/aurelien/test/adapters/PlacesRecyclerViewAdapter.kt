package com.aurelien.test.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aurelien.test.R
import com.aurelien.test.data.models.Place
import com.aurelien.test.databinding.PlaceItemBinding

class PlacesRecyclerViewAdapter(
    private var places: MutableList<Place>,
    private val callbacks: PlacesRecyclerViewAdapterCallback
) : RecyclerView.Adapter<PlaceItemViewHolder>(),
    PlaceItemViewHolder.PlaceItemItemViewHolderCallback {

    interface PlacesRecyclerViewAdapterCallback {
        fun placeClicked(place: Place)
        fun addPlaceAsFavorite(place: Place)
        fun removePlaceAsFavorite(placeId: String)
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

    fun setPlaces(places: MutableList<Place>) {
        this.places = places
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = places.size

    override fun placeClicked(place: Place) {
        callbacks.placeClicked(place)
    }

    override fun addPlaceAsFavorite(place: Place) {
        places.find { it.id == place.id }?.isFavorite = true
        callbacks.addPlaceAsFavorite(place)
    }

    override fun removePlaceAsFavorite(placeId: String) {
        places.find { it.id == placeId }?.isFavorite = false
        callbacks.removePlaceAsFavorite(placeId)
    }

    fun removePlaceAtIndex(index: Int) {
        places.removeAt(index)
        notifyItemRemoved(index)
    }
}

class PlaceItemViewHolder(
    private val binding: PlaceItemBinding,
    private val callbacks: PlaceItemItemViewHolderCallback
) : RecyclerView.ViewHolder(binding.root) {

    interface PlaceItemItemViewHolderCallback {
        fun placeClicked(place: Place)
        fun addPlaceAsFavorite(place: Place)
        fun removePlaceAsFavorite(placeId: String)
    }

    fun bind(place: Place) {
        binding.name.text = place.name
        binding.favoriteIcon.setImageResource(getFavoriteIcon(place.isFavorite))

        binding.favoriteIcon.setOnClickListener {
            place.isFavorite = !place.isFavorite
            binding.favoriteIcon.setImageResource(getFavoriteIcon(place.isFavorite))
            if (place.isFavorite) {
                callbacks.addPlaceAsFavorite(place)
            } else {
                callbacks.removePlaceAsFavorite(place.id)
            }
        }

        binding.layout.setOnClickListener {
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