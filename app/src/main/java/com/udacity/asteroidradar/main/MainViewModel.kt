package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.app.Application
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.ImagofDayApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.toAsteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.util.*

enum class AsteroidFilter { ViewWeekAsteroid, ViewTodayAsteroid, ViewSavedAsteroid }

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _erroMessage = MutableLiveData<String>()
    val erroMessage: LiveData<String> = _erroMessage

    private val _imageofDay = MutableLiveData<PictureOfDay>()
    val imageofDay: LiveData<PictureOfDay> = _imageofDay

    private val _navigateToSelected = MutableLiveData<Asteroid>()
    val navigateToSelected: LiveData<Asteroid> = _navigateToSelected

    private val database = AsteroidDatabase.create(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _asteroid = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>> = _asteroid


    private fun getImageofDay() = viewModelScope.launch {
        try {
            _imageofDay.value = ImagofDayApi.imageofDayService.getPictureofDay(Constants.API_KEY)
        } catch (e: Exception) {
            _imageofDay.value = null
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun todayAsteroid() = viewModelScope.launch {
        asteroidRepository.filterAsteroid(AsteroidFilter.ViewTodayAsteroid)
        _asteroid.value = asteroidRepository.asteroids

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun weekAsteroid() = viewModelScope.launch {
        asteroidRepository.filterAsteroid(AsteroidFilter.ViewWeekAsteroid)
        _asteroid.value = asteroidRepository.asteroids
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun savedAsteroid() = viewModelScope.launch {
        asteroidRepository.filterAsteroid(AsteroidFilter.ViewSavedAsteroid)
        _asteroid.value = asteroidRepository.asteroids
    }

    @SuppressLint("WeekBasedYear")
    @RequiresApi(Build.VERSION_CODES.N)

    fun resetError() {
        _erroMessage.value = null
    }

    private fun refreshDataframeRepository() = viewModelScope.launch {
        try {
            asteroidRepository.refreshAsteroidDatabase()
        } catch (e: IOException) {
            _erroMessage.value = "Asteroid Failure: ${e.message}"
        }

    }

    fun displayAsteroid(asteroid: Asteroid) {
        _navigateToSelected.value = asteroid
    }

    fun displayAsteroidComplete() {
        _navigateToSelected.value = null
    }

    init {
        refreshDataframeRepository()
        getImageofDay()
        savedAsteroid()
    }

}

class ViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(app) as T
        }
        throw IllegalArgumentException("unKnown Class")
    }
}