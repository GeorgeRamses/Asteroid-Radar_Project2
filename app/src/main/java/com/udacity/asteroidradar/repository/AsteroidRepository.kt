package com.udacity.asteroidradar.repository

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.toAsteroid
import com.udacity.asteroidradar.main.AsteroidFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {

    var asteroids = listOf<Asteroid>()

    @SuppressLint("WeekBasedYear")
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun filterAsteroid(filter: AsteroidFilter) {
        withContext(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            val starDate = calendar.time
            calendar.add(Constants.DEFAULT_END_DATE_DAYS, 7)
            val endDate = calendar.time
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            val formatedStartDate = dateFormat.format(starDate)
            val formatedEndDate = dateFormat.format(endDate)

            when (filter) {
                AsteroidFilter.ViewTodayAsteroid -> {
                    asteroids = database.asteroidDao.filterAsteroid(formatedStartDate, formatedStartDate).toAsteroid()
                }

                AsteroidFilter.ViewWeekAsteroid -> {
                    asteroids = database.asteroidDao.filterAsteroid(formatedStartDate, formatedEndDate).toAsteroid()
                }

                AsteroidFilter.ViewSavedAsteroid -> {
                    asteroids = database.asteroidDao.getAllAsteroid().toAsteroid()
                }
            }
        }
    }

    suspend fun refreshAsteroidDatabase() {
        withContext(Dispatchers.IO) {
            val asteroidString = AsteroidApi.asteroidApiService.getAsteroidList("", "", Constants.API_KEY)
            val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidString))
            database.asteroidDao.insertAll(asteroidList)
        }
    }
}