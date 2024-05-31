package com.sergimarrahyarenas.bloodstats.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sergimarrahyarenas.bloodstats.data.database.dao.FavoriteDao
import com.sergimarrahyarenas.bloodstats.data.database.dao.PreferencesDao
import com.sergimarrahyarenas.bloodstats.data.database.dao.UserDao
import com.sergimarrahyarenas.bloodstats.data.database.entities.FavoriteEntity
import com.sergimarrahyarenas.bloodstats.data.database.entities.PreferencesEntity
import com.sergimarrahyarenas.bloodstats.data.database.entities.UserEntity
import com.sergimarrahyarenas.bloodstats.data.database.entities.UserFavoriteCrossRef

@Database(
    entities = [UserEntity::class, PreferencesEntity::class, FavoriteEntity::class, UserFavoriteCrossRef::class],
    version = 6,
    exportSchema = false
)

abstract class BloodStatsDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun preferencesDao(): PreferencesDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private var instance: BloodStatsDatabase? = null

        fun getInstance(context: Context): BloodStatsDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    BloodStatsDatabase::class.java,
                    "bloodstats-db"
                ).fallbackToDestructiveMigration().build().also { instance = it }
            }
        }
    }
}