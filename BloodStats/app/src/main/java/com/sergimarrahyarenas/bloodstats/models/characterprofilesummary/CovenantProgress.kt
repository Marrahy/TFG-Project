package com.sergimarrahyarenas.bloodstats.models.characterprofilesummary

data class CovenantProgress(
    val chosen_covenant: com.sergimarrahyarenas.bloodstats.models.characterprofilesummary.ChosenCovenant,
    val renown_level: Int,
    val soulbinds: com.sergimarrahyarenas.bloodstats.models.characterprofilesummary.Soulbinds
)