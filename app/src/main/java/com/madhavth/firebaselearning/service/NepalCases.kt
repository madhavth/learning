package com.madhavth.firebaselearning.service

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Json(name = "countries_stat")
data class NepalCases (
    @Json(name = "country_name")
    val countryName: String,
    val cases: String,
    val deaths: String,
    val region: String,
    @Json(name = "total_recovered")
    val totalRecovered: String,
    @Json(name = "new_deaths")
    val newDeaths: String,
    @Json(name = "new_cases")
    val newCases: String,
    @Json(name="serious_critical")
    val seriousCritical: String
)