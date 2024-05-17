package com.sergimarrahyarenas.bloodstats.database.pojos

import androidx.room.Embedded
import androidx.room.Relation
import com.sergimarrahyarenas.bloodstats.database.entities.PreferencesEntity
import com.sergimarrahyarenas.bloodstats.database.entities.UserEntity

data class UserWithPreferences(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "userUUID",
        entityColumn = "userId"
    )
    val preferences: PreferencesEntity?
)
