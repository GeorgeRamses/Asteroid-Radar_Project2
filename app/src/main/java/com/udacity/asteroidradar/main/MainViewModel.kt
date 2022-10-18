package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _asteroidProperty = MutableLiveData<String>()
    val asteroidProperty: LiveData<String> = _asteroidProperty

    fun getAsteroidData() = viewModelScope.launch {
        try {
            val astroidList =
                AsteroidApi.asteroidApiService.getAsteroidProperties(Constants.API_KEY)
            _asteroidProperty.value = astroidList[0].estimatedDiameter.toString()
        } catch (e: Exception) {
            _asteroidProperty.value = "Failure: ${e.message}"
        }
    }

    init {
        getAsteroidData()
    }
}