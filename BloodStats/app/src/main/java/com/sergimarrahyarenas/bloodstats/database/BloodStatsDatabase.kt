package com.sergimarrahyarenas.bloodstats.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sergimarrahyarenas.bloodstats.database.converters.Converters
import com.sergimarrahyarenas.bloodstats.database.dao.UserDao
import com.sergimarrahyarenas.bloodstats.database.entities.FavoriteEntity
import com.sergimarrahyarenas.bloodstats.database.entities.PreferencesEntity
import com.sergimarrahyarenas.bloodstats.database.entities.UserEntity
import com.sergimarrahyarenas.bloodstats.database.entities.UserFavoriteCrossRef

@Database(
    entities = [UserEntity::class, PreferencesEntity::class, FavoriteEntity::class, UserFavoriteCrossRef::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BloodStatsDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

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