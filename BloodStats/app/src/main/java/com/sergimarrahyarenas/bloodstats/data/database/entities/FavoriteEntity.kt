package com.sergimarrahyarenas.bloodstats.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "favorite",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userUUID"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false) val favoriteUUID: String = UUID.randomUUID().toString(),
    val userId: String,
    val characterName: String,
    val characterRealmSlug: String,
    val characterMythicRating: Int
)
