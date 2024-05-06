package com.sergimarrahyarenas.bloodstats.models.characterdata

data class Guild(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.models.characterdata.Key,
    val name: String,
    val realm: com.sergimarrahyarenas.bloodstats.models.characterdata.RealmX
)