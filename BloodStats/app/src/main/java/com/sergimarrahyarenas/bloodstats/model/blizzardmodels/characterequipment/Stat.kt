package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment

data class Stat(
    val display: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.Display,
    val is_equip_bonus: Boolean,
    val is_negated: Boolean,
    val type: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.Type,
    val value: Int
)