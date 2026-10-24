package ir.javagym.weatherforecastapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.javagym.weatherforecastapp.model.Favorite
import ir.javagym.weatherforecastapp.model.Unit

@Database(entities = [Favorite::class, Unit::class], version = 2, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}