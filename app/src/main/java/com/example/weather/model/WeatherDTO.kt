package com.example.weather.model

import com.google.gson.annotations.SerializedName

data class WeatherEveryThreeHoursDTO (
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("response")
    val response: List<Response>
)

data class WeatherNowDTO (
    @SerializedName("meta")
    val meta:Meta,
    @SerializedName("response")
    val response: Response
)


data class Meta (
    val message: String,
    val code: String
)

data class Response (
    val pressure: Pressure,
    val humidity: Humidity,
    val icon: String,
    val wind: Wind,
    val cloudiness: Cloudiness,
    val date: Date,
    val temperature: Temperature,
    val description: Description
)

data class Cloudiness (
    val type: Long,
    val percent: Long
)

data class Date (
    @SerializedName("UTC")
    val utc: String,

    @SerializedName("time_zone_offset")
    val timeZoneOffset: Long,

    val local: String,

    @SerializedName("hr_to_forecast")
    val hrToForecast: Any? = null,

    val unix: Long
)

data class Description (
    val full: String
)

data class Humidity (
    val percent: Long
)

data class Pressure (
    @SerializedName("h_pa")
    val hPa: Long,

    @SerializedName("mm_hg_atm")
    val mmHgATM: Long,

    @SerializedName("in_hg")
    val inHg: Double
)

data class Temperature (
    val comfort: Air,
    val water: Air,
    val air: Air
)

data class Air (
    @SerializedName("C")
    val c: Double,

    @SerializedName("F")
    val f: Double
)

data class Wind (
    val direction: Direction,
    val speed: Speed
)

data class Direction (
    val degree: Long,

    @SerializedName("scale_8")
    val scale8: Long
)

data class Speed (
    @SerializedName("km_h")
    val kmH: Long,

    @SerializedName("m_s")
    val mS: Long,

    @SerializedName("mi_h")
    val miH: Long
)
