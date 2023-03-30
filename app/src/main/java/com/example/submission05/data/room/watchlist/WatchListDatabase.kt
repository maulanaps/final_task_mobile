package com.example.submission05.data.room.watchlist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.submission03.model.MovieAndTvShowEntity

@Database(entities = [MovieAndTvShowEntity::class], version = 1)
abstract class WatchListDatabase : RoomDatabase() {
    abstract fun WatchListDao(): WatchListDao

    companion object {

        @Volatile
        private var INSTANCE: WatchListDatabase? = null

        fun getInstance(context: Context): WatchListDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = buildDatabase(context)
                INSTANCE = instance
                instance
            }
        }

        private fun buildDatabase(context: Context): WatchListDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                WatchListDatabase::class.java,
                "watchListKu.db"
            ).build()
        }
    }
}