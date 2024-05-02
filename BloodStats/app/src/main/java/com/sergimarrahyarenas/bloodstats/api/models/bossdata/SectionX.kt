package com.sergimarrahyarenas.bloodstats.api.models.bossdata

data class SectionX(
    val body_text: String,
    val creature_display: CreatureDisplay,
    val id: Int,
    val sections: List<SectionXX>,
    val title: String
)