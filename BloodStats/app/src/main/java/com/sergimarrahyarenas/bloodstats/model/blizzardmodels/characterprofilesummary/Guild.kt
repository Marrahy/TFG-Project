package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary

data class Guild(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Key,
    val name: String,
    val realm: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.RealmX
)