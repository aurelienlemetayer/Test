package com.aurelien.test.adapters

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aurelien.test.data.models.Departure
import com.aurelien.test.databinding.DepartureItemBinding
import java.text.SimpleDateFormat
import java.util.*

class DeparturesRecyclerViewAdapter(private var departures: MutableList<Departure>) :
    RecyclerView.Adapter<DepartureItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DepartureItemViewHolder(
            DepartureItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DepartureItemViewHolder, position: Int) {
        holder.bind(departures[position])
    }

    fun setDepartures(departures: MutableList<Departure>) {
        this.departures = departures
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = departures.size
}

class DepartureItemViewHolder(private val binding: DepartureItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(departure: Departure) {
        binding.commercialModeIcon.setImageDrawable(
            ContextCompat.getDrawable(
                itemView.context,
                departure.commercialMode.drawableResId
            )
        )
        binding.code.text = departure.code
        binding.code.background.colorFilter =
            PorterDuffColorFilter(Color.parseColor("#${departure.color}"), PorterDuff.Mode.SRC_ATOP)

        val localizedPattern = SimpleDateFormat("HH:mm", Locale.getDefault()).toLocalizedPattern()
        binding.hour.text =
            SimpleDateFormat(localizedPattern, Locale.getDefault()).format(departure.date)

        binding.direction.text = departure.direction
    }
}