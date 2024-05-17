package com.sergimarrahyarenas.bloodstats.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "preferences",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userUUID"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PreferencesEntity(
    @PrimaryKey(autoGenerate = false) val preferencesUUID: String = UUID.randomUUID().toString(),
    val userId: String,
    val theme: String
)