package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    companion object{
        const val WORK_NAME="com.udacity.asteroidradar.work.RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.create(applicationContext)
        val asteroidRepository = AsteroidRepository(database)
        try {
            asteroidRepository.refreshAsteroidDatabase()
            return Result.success()
        } catch (e: HttpException) {
            return Result.retry()
        }

    }
}