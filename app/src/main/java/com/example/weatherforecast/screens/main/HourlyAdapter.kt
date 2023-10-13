package com.example.weatherforecast.screens.main

import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.weather.HourlyForecast
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ItemHourlyForecastBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class HourlyAdapter : RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    private val hourlyData = ArrayList<HourlyForecast>()

    inner class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemHourlyForecastBinding.bind(itemView)

        fun bind(data: HourlyForecast) {
            binding.apply {
                Log.d("LOG", data.currentDate.toString())
                tvHour.text =
                    if (Build.VERSION.SDK_INT >= VERSION_CODES.O) data.currentDate.hour.toString() + ":00" else
                        data.currentDate.toHour()
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
        return hourlyData.size
    }


    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        holder.bind(hourlyData[position])
    }

    fun submitList(hourly: List<HourlyForecast>) {
        hourlyData.clear()
        hourlyData.addAll(hourly)
        notifyDataSetChanged()
    }

    fun LocalDateTime.toHour(): String {
        val time = this.toString()
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val parsedDate = inputFormat.parse(time)!!
        return outputFormat.format(parsedDate)


    }
}

