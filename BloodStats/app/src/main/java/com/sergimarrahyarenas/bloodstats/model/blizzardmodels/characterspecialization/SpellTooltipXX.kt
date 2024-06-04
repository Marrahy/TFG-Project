package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization

data class SpellTooltipXX(
    val cast_time: String,
    val cooldown: String,
    val description: String,
    val power_cost: String,
    val range: String,
    val spell: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.Spell
)