package com.example.app.Commands.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app.Commands.Database.Dao.ItemsDao
import com.example.app.Commands.Database.Entities.Items

@Database(entities = [Items :: class], version=1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun itemsDao(): ItemsDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase (context : Context) :AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "geo_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}