package com.sergimarrahyarenas.bloodstats.database.pojos

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.sergimarrahyarenas.bloodstats.database.entities.FavoriteEntity
import com.sergimarrahyarenas.bloodstats.database.entities.UserEntity
import com.sergimarrahyarenas.bloodstats.database.entities.UserFavoriteCrossRef


data class UserWithFavorites(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "userUUID",
        entityColumn = "favoriteUUID",
        associateBy = Junction(UserFavoriteCrossRef::class)
    )
    val favorites: List<FavoriteEntity>
)
