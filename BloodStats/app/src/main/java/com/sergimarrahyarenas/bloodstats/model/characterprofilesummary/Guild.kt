package com.sergimarrahyarenas.bloodstats.model.characterprofilesummary

data class Guild(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.characterprofilesummary.Key,
    val name: String,
    val realm: com.sergimarrahyarenas.bloodstats.model.characterprofilesummary.RealmX
)