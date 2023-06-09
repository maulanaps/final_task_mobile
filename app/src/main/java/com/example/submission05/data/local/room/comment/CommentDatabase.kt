package com.example.submission05.data.local.room.comment

import android.content.Context
import androidx.room.*
import com.example.submission05.data.local.entity.CommentEntity

@Database(
    entities = [CommentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CommentDatabase : RoomDatabase() {

    abstract fun CommentDao(): CommentDao
    companion object {

        @Volatile
        private var INSTANCE: CommentDatabase? = null

        fun getInstance(context: Context): CommentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = buildDatabase(context)
                INSTANCE = instance
                instance
            }
        }

        private fun buildDatabase(context: Context): CommentDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                CommentDatabase::class.java,
                "commentKu.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}