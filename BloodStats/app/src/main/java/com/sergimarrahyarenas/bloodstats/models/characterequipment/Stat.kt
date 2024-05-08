package com.sergimarrahyarenas.bloodstats.models.characterequipment

data class Stat(
    val display: Display,
    val is_equip_bonus: Boolean,
    val is_negated: Boolean,
    val type: Type,
    val value: Int
)