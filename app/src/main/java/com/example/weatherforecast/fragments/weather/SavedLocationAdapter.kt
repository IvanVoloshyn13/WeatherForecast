package com.example.weatherforecast.fragments.weather

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.SearchedCity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ItemCitiesBinding

class SavedLocationAdapter(private val listener: OnCityClickListener) :
    RecyclerView.Adapter<SavedLocationAdapter.CitiesItemViewHolde>(), View.OnClickListener {
    private var citiesList = ArrayList<SearchedCity>()

    inner class CitiesItemViewHolde(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCitiesBinding.bind(itemView)
        fun bind(data: SearchedCity) {
            binding.apply {
                tvCity.text = data.cityName
                ivIcon.setImageResource(R.drawable.ic_current_location)
            }
        }

        init {
            itemView.setOnClickListener(this@SavedLocationAdapter)
        }
    }

    override fun onClick(v: View) {
        val city = v.tag as SearchedCity
        listener.onClick(city)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesItemViewHolde {
        return CitiesItemViewHolde(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cities, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return  citiesList.size
    }

    override fun onBindViewHolder(holder: CitiesItemViewHolde, position: Int) {
        val city = citiesList[position]
        holder.bind(city)
        holder.itemView.tag = city
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<SearchedCity>) {
        citiesList.clear()
        citiesList.addAll(list)
        notifyDataSetChanged() //TODO() setup this method with more specific change event if it have sense
    }

}

interface OnCityClickListener {
    fun onClick(city: SearchedCity)
}