package com.example.weather.view

import com.example.weather.model.CityDTO

interface OnItemClickListener {
    fun onItemClick(cityDTO: CityDTO)
}