package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment

data class Damage(
    val damage_class: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.DamageClass,
    val display_string: String,
    val max_value: Int,
    val min_value: Int
)