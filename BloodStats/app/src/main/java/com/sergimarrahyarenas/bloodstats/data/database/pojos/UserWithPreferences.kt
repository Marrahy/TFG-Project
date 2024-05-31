package com.sergimarrahyarenas.bloodstats.data.database.pojos

import androidx.room.Embedded
import androidx.room.Relation
import com.sergimarrahyarenas.bloodstats.data.database.entities.PreferencesEntity
import com.sergimarrahyarenas.bloodstats.data.database.entities.UserEntity

data class UserWithPreferences(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "userUUID",
        entityColumn = "userId"
    )
    val preferences: PreferencesEntity?
)
