package com.example.weatherforecast.fragments.addsearchcity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.searchscreen.SearchedCity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ItemSearchCityBinding

class SearchedCitiesAdapter(private val listener: RecyclerViewOnItemClick) :
    ListAdapter<SearchedCity, SearchedCitiesAdapter.CitiesViewHolder>(CitiesDiffUtil()) {

    val citiesList = ArrayList<SearchedCity>()

    inner class CitiesViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }

        private val binding = ItemSearchCityBinding.bind(view)
        fun bind(item: SearchedCity) {
            binding.tvCity.text = item.toString()
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(citiesList[position])
            }
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
        notifyDataSetChanged() //TODO() setup this method with more specific change event
    }

    interface RecyclerViewOnItemClick {
        fun onItemClick(city: SearchedCity)
    }
}