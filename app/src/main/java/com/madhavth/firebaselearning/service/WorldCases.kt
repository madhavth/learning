package com.madhavth.firebaselearning.service

import com.squareup.moshi.Json

class WorldCases {
    @Json(name = "total_cases")
    var totalCases: String? = null

    @Json(name = "total_deaths")
    var totalDeaths: String? = null

    @Json(name = "total_recovered")
    var totalRecovered: String? = null

    @Json(name = "new_cases")
    var newCases: String? = null

    @Json(name = "new_deaths")
    var newDeaths: String? = null

    @Json(name = "statistic_taken_at")
    var statisticTakenAt: String? = null

}