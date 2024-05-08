package com.sergimarrahyarenas.bloodstats.models.characterspecialization

data class SpellTooltip(
    val cast_time: String,
    val cooldown: String,
    val description: String,
    val power_cost: String,
    val range: String,
    val spell: Spell
)