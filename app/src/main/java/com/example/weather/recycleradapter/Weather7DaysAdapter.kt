package com.example.weather.recycleradapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeRecyclerWeather7DaysBinding
import com.example.weather.model.Response7
import java.text.SimpleDateFormat
import java.util.*

class Weather7DaysAdapter : RecyclerView.Adapter<Weather7DaysAdapter.Weather7DaysHolder>() {

    private var responseData: List<Response7> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setWeather(data: List<Response7>) {
        this.responseData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Weather7DaysHolder {
        return Weather7DaysHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_home_recycler_weather_7_days, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Weather7DaysHolder, position: Int) {
        holder.bind(this.responseData[position])
    }

    override fun getItemCount(): Int {
        return if (responseData.size > 8) 8
        else responseData.size
    }

    inner class Weather7DaysHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val sp = itemView.context.getSharedPreferences("Setting_preferences", Context.MODE_PRIVATE)
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(responseData: Response7) {
            FragmentHomeRecyclerWeather7DaysBinding.bind(itemView).run {
                val sdf = SimpleDateFormat("dd MMM EEE")
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = responseData.date.unix*1000
                date.text = sdf.format(calendar.time)

                val resId = itemView.resources.getIdentifier(responseData.icon, "drawable", itemView.context.packageName)
                weatherImg.loadIconSvg(resId)

                humidity.text =
                    "${itemView.resources.getString(R.string.humidity)} " +
                            "${responseData.humidity.percent.avg}%"

                when(sp.getString("temperature",itemView.context.resources.getString(R.string.degree_c))){
                    "??C"->{
                        temperature.text = "${responseData.temperature.air.avg.c}??/${responseData.temperature.comfort.min.c}??"
                    }
                    "??F"->{
                        temperature.text = "${responseData.temperature.air.avg.f}??/${responseData.temperature.comfort.min.f}??"
                    }
                }
            }
        }
        private fun ImageView.loadIconSvg(url: Int){
            val imageLoader = ImageLoader.Builder(this.context)
                .componentRegistry{add(SvgDecoder(this@loadIconSvg.context))}
                .build()

            val request = ImageRequest.Builder(this.context)
                .data(url)
                .target(this)
                .build()
            imageLoader.enqueue(request)
        }
    }
}