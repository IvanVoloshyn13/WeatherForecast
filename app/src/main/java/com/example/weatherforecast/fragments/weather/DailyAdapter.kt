package com.example.weatherforecast.fragments.weather

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.weather.DailyForecast
import com.example.weatherforecast.R
import com.example.weatherforecast.WeatherType
import com.example.weatherforecast.databinding.ItemDailyForecastBinding

class DailyAdapter : RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {
    private var dailyForecast = ArrayList<DailyForecast>()

    inner class DailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemDailyForecastBinding.bind(itemView)
        fun bind(data: DailyForecast) {
            binding.apply {
                ivWeatherType.setImageResource(WeatherType.fromWHO(data.weatherCode).weatherIcon)
                tvMaxTemp.text = data.maxTemperature.toString()
                tvMinTemp.text = data.minTemperature.toString()
                tvDayOfWeek.text = data.dayOfTheWeek
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_daily_forecast, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dailyForecast.size
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        holder.bind(dailyForecast[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list:List<DailyForecast>){
        dailyForecast.clear()
        dailyForecast.addAll(list)
        notifyDataSetChanged() //TODO() setup this method with more specific change event if it have sense
    }
}