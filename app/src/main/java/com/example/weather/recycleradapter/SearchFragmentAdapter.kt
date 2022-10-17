package com.example.weather.recycleradapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FragmentSearchCityItemBinding
import com.example.weather.model.CityDTO
import com.example.weather.view.OnItemClickListener
import java.util.*

@SuppressLint("NotifyDataSetChanged")
class SearchFragmentAdapter(val listener: OnItemClickListener) : RecyclerView.Adapter<SearchFragmentAdapter.ViewHolder>(),Filterable
{

    private var data: MutableList<CityDTO> = mutableListOf()
    private var filteredData: MutableList<CityDTO> = mutableListOf()

    fun setCities(data: List<CityDTO>) {
        this.data = data as MutableList<CityDTO>
        this.filteredData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_search_city_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(city: CityDTO) {
            FragmentSearchCityItemBinding.bind(itemView).run {
                cityName.text = city.name
                root.setOnClickListener {
                    listener.onItemClick(city)
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                data = if (charSequence == "") {
                    filteredData
                } else {
                    val resultFilteredTimes: MutableList<CityDTO> = mutableListOf()
                    filteredData.forEach {
                        if (it.name.lowercase(Locale.ROOT).contains(
                                charSequence.toString().lowercase(Locale.ROOT))
                        ) resultFilteredTimes.add(it)
                    }
                    resultFilteredTimes

                }
                val filterResults = FilterResults()
                filterResults.values = data
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
                data = filterResults?.values as MutableList<CityDTO>
                notifyDataSetChanged()
            }

        }
    }

}