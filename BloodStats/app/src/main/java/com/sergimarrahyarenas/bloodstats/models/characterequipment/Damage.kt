package com.sergimarrahyarenas.bloodstats.models.characterequipment

data class Damage(
    val damage_class: DamageClass,
    val display_string: String,
    val max_value: Int,
    val min_value: Int
)