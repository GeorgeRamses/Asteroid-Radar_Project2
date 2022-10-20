package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel : ViewModel() {
    private val _asteroidProperty = MutableLiveData<List<Asteroid>>()
    val asteroidProperty: LiveData<List<Asteroid>> = _asteroidProperty

    private val _errorMassage = MutableLiveData<String>()
    val errorMassage: LiveData<String> = _errorMassage

    private val _astroidCodeName = MutableLiveData<String>()
    val astroidCodeName: LiveData<String> = _astroidCodeName

    private val _approach_date = MutableLiveData<String>()
    val approach_date: LiveData<String> = _approach_date

    private val _isPotentiallyHazardous = MutableLiveData<Boolean>()
    val isPotentiallyHazardous: LiveData<Boolean> = _isPotentiallyHazardous

    fun getAsteroidData() = viewModelScope.launch {
        try {
            val astroidList =
                AsteroidApi.asteroidApiService.getAsteroidProperties(Constants.API_KEY)
            _asteroidProperty.value = parseAsteroidsJsonResult(JSONObject(astroidList))
        } catch (e: Exception) {
            _errorMassage.value = "Failure: ${e.message}"
        }
    }


    init {
        getAsteroidData()
    }
}