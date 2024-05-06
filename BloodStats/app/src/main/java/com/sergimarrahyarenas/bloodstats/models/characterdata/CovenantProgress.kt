package com.sergimarrahyarenas.bloodstats.models.characterdata

data class CovenantProgress(
    val chosen_covenant: com.sergimarrahyarenas.bloodstats.models.characterdata.ChosenCovenant,
    val renown_level: Int,
    val soulbinds: com.sergimarrahyarenas.bloodstats.models.characterdata.Soulbinds
)