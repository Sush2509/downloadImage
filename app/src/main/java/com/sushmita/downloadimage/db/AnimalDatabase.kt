package com.sushmita.downloadimage.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sushmita.downloadimage.db.dao.AnimalDao
import com.sushmita.downloadimage.db.dto.Animal

@Database(entities = [Animal::class], version = 1, exportSchema = false)
abstract class AnimalDatabase : RoomDatabase() {

    abstract fun getAnimalDao(): AnimalDao

    companion object {
        @Volatile
        private var INSTANCE: AnimalDatabase? = null

        private const val DB_NAME = "animal_database.db"

        fun getDatabase(context: Context): AnimalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimalDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}