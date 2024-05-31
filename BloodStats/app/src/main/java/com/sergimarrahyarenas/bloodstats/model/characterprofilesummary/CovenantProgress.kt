package com.sergimarrahyarenas.bloodstats.model.characterprofilesummary

data class CovenantProgress(
    val chosen_covenant: com.sergimarrahyarenas.bloodstats.model.characterprofilesummary.ChosenCovenant,
    val renown_level: Int,
    val soulbinds: com.sergimarrahyarenas.bloodstats.model.characterprofilesummary.Soulbinds
)