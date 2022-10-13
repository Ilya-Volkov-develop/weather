package com.example.weather.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeRecyclerWeatherEveryThreeHoursBinding
import com.example.weather.model.Response

class WeatherEveryThreeHoursAdapter :
    RecyclerView.Adapter<WeatherEveryThreeHoursAdapter.WeatherEveryThreeHoursHolder>() {

    private var responseData: List<Response> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setWeather(data: List<Response>) {
        this.responseData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): WeatherEveryThreeHoursHolder {
        return WeatherEveryThreeHoursHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_home_recycler_weather_every_three_hours, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WeatherEveryThreeHoursHolder, position: Int) {
        holder.bind(this.responseData[position], position)
    }

    override fun getItemCount(): Int {
        return if (responseData.size > 8) 8
        else responseData.size
    }

    inner class WeatherEveryThreeHoursHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(responseData: Response, position: Int) {
            FragmentHomeRecyclerWeatherEveryThreeHoursBinding.bind(itemView).run {
                if (position == 0) {
                    time.text = itemView.resources.getString(R.string.now)
                } else {
                    time.text = responseData.date.local.subSequence(11, 16)
                }

                val resId = itemView.resources.getIdentifier(responseData.icon,
                    "drawable",
                    itemView.context.packageName)
                weatherImg.loadIconSvg(resId)
                humidity.text =
                    "${itemView.resources.getString(R.string.humidity)} " +
                            "${responseData.humidity.percent}%"
                temperature.text =
                    "${responseData.temperature.comfort.c}°/${responseData.temperature.air.c}°"
            }
        }

        private fun ImageView.loadIconSvg(url: Int) {
            val imageLoader = ImageLoader.Builder(this.context)
                .componentRegistry { add(SvgDecoder(this@loadIconSvg.context)) }
                .build()

            val request = ImageRequest.Builder(this.context)
                .data(url)
                .target(this)
                .build()
            imageLoader.enqueue(request)
        }
    }
}