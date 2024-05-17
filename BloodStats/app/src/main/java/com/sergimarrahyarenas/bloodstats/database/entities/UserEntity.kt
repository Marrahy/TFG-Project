package com.sergimarrahyarenas.bloodstats.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = false) val userUUID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo("user_password") val userPassword: String,
    @ColumnInfo("user_email") val userEmail: String? = null
)