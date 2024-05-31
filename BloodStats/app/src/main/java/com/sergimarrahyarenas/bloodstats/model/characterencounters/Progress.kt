package com.sergimarrahyarenas.bloodstats.model.characterencounters

data class Progress(
    val completed_count: Int,
    val encounters: List<Encounter>,
    val total_count: Int
)