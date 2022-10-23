package com.udacity.asteroidradar.network

import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.DatabaseAsteroid

data class NetworkAsteroid(
    val id: Long,
    @Json(name = "name")
    val codename: String,
    @Json(name = "close_approach_data")
    val closeApproachDate: String,
    @Json(name = "absolute_magnitude")
    val absoluteMagnitude: Double,
    @Json(name = "estimated_diameter")
    val estimatedDiameter: Double,
    @Json(name = "relative_velocity")
    val relativeVelocity: Double,
    @Json(name = "miss_distance")
    val distanceFromEarth: Double,
    @Json(name = "is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean
)

fun List<NetworkAsteroid>.toAsteroid(): List<Asteroid> {
    return map {
        Asteroid(
            it.id,
            it.codename,
            it.closeApproachDate,
            it.absoluteMagnitude,
            it.estimatedDiameter,
            it.relativeVelocity,
            it.distanceFromEarth,
            it.isPotentiallyHazardous
        )
    }
}

fun List<NetworkAsteroid>.toDatabaseAsteroid(): List<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid(
            it.id,
            it.codename,
            it.closeApproachDate,
            it.absoluteMagnitude,
            it.estimatedDiameter,
            it.relativeVelocity,
            it.distanceFromEarth,
            it.isPotentiallyHazardous
        )
    }
}