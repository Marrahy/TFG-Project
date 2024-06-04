package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.realm

data class RealmInfo(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.realm.Key,
    val name: String,
    val slug: String
)