package com.example.weather.model

import com.google.gson.annotations.SerializedName

data class Weather7DTO (
    val meta: Meta,
    val response: List<Response7>
)

data class Response7 (
    val pressure: Pressure7,
    val humidity: Humidity7,
    val description: Description,
    val cloudiness: Cloudiness,
    val date: Date,
    val temperature: Temperature7,
    val wind: Wind7,
    val icon: String
)

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


//----------------------------------------------------------------------------------------
//data class Date (
//    @SerializedName("UTC")
//    val utc: String,
//
//    val local: String,
//
//    @SerializedName("time_zone_offset")
//    val timeZoneOffset: Long,
//
//    val unix: Long
//)

data class Humidity7 (
    val percent: Percent
)

data class Percent (
    val max: Long,
    val min: Long,
    val avg: Long
)

data class Pressure7 (
    @SerializedName("h_pa")
    val hPa: HPa,

    @SerializedName("mm_hg_atm")
    val mmHgATM: HPa,

    @SerializedName("in_hg")
    val inHg: HPa
)

data class HPa (
    val max: Double,
    val min: Double
)

data class Temperature7 (
    val comfort: Comfort,
    val water: Comfort,
    val air: Air7
)

data class Air7 (
    val max: AirAvg,
    val min: AirAvg,
    val avg: AirAvg
)

data class AirAvg (
    @SerializedName("C")
    val c: Double,

    @SerializedName("F")
    val f: Double
)

data class Comfort (
    val max: AirAvg,
    val min: AirAvg
)

data class Wind7 (
    val speed: Speed7,
    val direction: Direction7
)

data class Direction7 (
    val max: DirectionAvg,
    val min: DirectionAvg,
    val avg: DirectionAvg
)

data class DirectionAvg (
    val degree: Long? = null,

    @SerializedName("scale_8")
    val scale8: Long? = null
)

data class Speed7 (
    val max: SpeedAvg,
    val min: SpeedAvg,
    val avg: SpeedAvg
)

data class SpeedAvg (
    @SerializedName("km_h")
    val kmH: Long? = null,

    @SerializedName("m_s")
    val mS: Long? = null,

    @SerializedName("mi_h")
    val miH: Long? = null
)
