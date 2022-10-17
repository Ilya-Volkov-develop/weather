package com.example.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CityDTO (
    val coords: Coords,
    val district: String,
    val name: String,
    val population: String,
    val subject: String
):Parcelable
@Parcelize
data class Coords (
    val lat: String,
    val lon: String
):Parcelable