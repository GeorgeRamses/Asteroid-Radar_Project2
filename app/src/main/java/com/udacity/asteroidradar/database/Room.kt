package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(listAsteroid: ArrayList<DatabaseAsteroid>)

    @Query("select * from DatabaseAsteroid order by closeApproachDate")
    fun getAllAsteroid(): List<DatabaseAsteroid>

    @Query("select * from DatabaseAsteroid where closeApproachDate between :startDate and :endDate order by closeApproachDate")
    fun filterAsteroid(startDate: String, endDate: String): List<DatabaseAsteroid>
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

    companion object {
        private lateinit var INSTANCE: AsteroidDatabase

        fun create(context: Context): AsteroidDatabase {
            return synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java, "AsteroidDB"
                    ).build()
                }
                INSTANCE
            }
        }
    }
}