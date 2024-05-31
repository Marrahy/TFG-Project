package com.sergimarrahyarenas.bloodstats.data.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["userUUID", "favoriteUUID"])
data class UserFavoriteCrossRef(
    val userUUID: String,
    val favoriteUUID: String
)
