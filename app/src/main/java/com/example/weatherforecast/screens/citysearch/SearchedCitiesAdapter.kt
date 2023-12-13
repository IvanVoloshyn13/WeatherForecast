package com.example.weatherforecast.screens.citysearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.searchscreen.SearchedCity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ItemSearchCityBinding

class SearchedCitiesAdapter :
    ListAdapter<SearchedCity, SearchedCitiesAdapter.CitiesViewHolder>(CitiesDiffUtil()) {

    val citiesList = ArrayList<SearchedCity>()

    inner class CitiesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSearchCityBinding.bind(view)
        fun bind(item: SearchedCity) {
            binding.tvCity.text = item.toString()
        }
    }

    class CitiesDiffUtil() : DiffUtil.ItemCallback<SearchedCity>() {
        override fun areItemsTheSame(oldItem: SearchedCity, newItem: SearchedCity): Boolean {
            return oldItem.latitude == newItem.latitude
        }

        override fun areContentsTheSame(oldItem: SearchedCity, newItem: SearchedCity): Boolean {
            return oldItem.cityName == newItem.cityName && oldItem.latitude == newItem.latitude
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_city, parent, false)
        return CitiesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        val item = citiesList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return citiesList.size
    }

    fun submitList(list: ArrayList<SearchedCity>) {
        citiesList.clear()
        citiesList.addAll(list)
        notifyDataSetChanged()
    }
}