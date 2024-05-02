package com.sergimarrahyarenas.bloodstats.api.models.bossdata

data class BossData(
    val _links: Links,
    val category: Category,
    val creatures: List<Creature>,
    val description: String,
    val id: Int,
    val instance: Instance,
    val items: List<Item>,
    val modes: List<Mode>,
    val name: String,
    val sections: List<Section>
)