package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterencounters

data class Progress(
    val completed_count: Int,
    val encounters: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterencounters.Encounter>,
    val total_count: Int
)