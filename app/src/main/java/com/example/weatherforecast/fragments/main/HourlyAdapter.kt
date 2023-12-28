package com.example.weatherforecast.fragments.main

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.mainscreen.weather.HourlyForecast
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ItemHourlyForecastBinding

class HourlyAdapter : RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    private val hourlyForecast = ArrayList<HourlyForecast>()

    inner class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemHourlyForecastBinding.bind(itemView)

       @SuppressLint("SetTextI18n")
        fun bind(data: HourlyForecast) {
            binding.apply {
                Log.d("LOG", data.currentDate.toString())
                tvHour.text =
                    data.currentDate.hour.toString() + ":00"
                tvTemperature.text = data.currentTemp.toString()
                tvWeatherTypeIcon.setImageResource(data.weatherType.weatherIcon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hourly_forecast, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return hourlyForecast.size
    }


    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        holder.bind(hourlyForecast[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(hourly: List<HourlyForecast>) {
        hourlyForecast.clear()
        hourlyForecast.addAll(hourly)
        notifyDataSetChanged() //TODO() setup this method with more specific change event if it have sense
    }

}

