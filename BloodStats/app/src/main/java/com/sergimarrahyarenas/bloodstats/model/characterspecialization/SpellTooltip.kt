package com.sergimarrahyarenas.bloodstats.model.characterspecialization

data class SpellTooltip(
    val cast_time: String,
    val cooldown: String,
    val description: String,
    val power_cost: String,
    val range: String,
    val spell: Spell
)