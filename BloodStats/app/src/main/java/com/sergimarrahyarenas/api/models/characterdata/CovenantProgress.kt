package com.sergimarrahyarenas.api.models.characterdata

data class CovenantProgress(
    val chosen_covenant: ChosenCovenant,
    val renown_level: Int,
    val soulbinds: Soulbinds
)