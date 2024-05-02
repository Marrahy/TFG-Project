package com.sergimarrahyarenas.bloodstats.api.models.bossdata

data class Section(
    val body_text: String,
    val id: Int,
    val sections: List<SectionX>,
    val title: String
)