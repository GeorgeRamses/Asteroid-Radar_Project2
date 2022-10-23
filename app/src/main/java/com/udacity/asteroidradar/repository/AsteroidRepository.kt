package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.toAsteroid
import com.udacity.asteroidradar.network.toDatabaseAsteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAllAsteroid()) {
        it.toAsteroid()
    }

    suspend fun refreshAsteroidDatabase() {
        withContext(Dispatchers.IO) {
            val asteroidList = AsteroidApi.asteroidApiService.getAsteroidList(Constants.API_KEY)
            database.asteroidDao.insertAll(asteroidList.toDatabaseAsteroid())
        }
    }
}