package com.sergimarrahyarenas.bloodstats.models.characterprofilesummary

data class Guild(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.models.characterprofilesummary.Key,
    val name: String,
    val realm: com.sergimarrahyarenas.bloodstats.models.characterprofilesummary.RealmX
)