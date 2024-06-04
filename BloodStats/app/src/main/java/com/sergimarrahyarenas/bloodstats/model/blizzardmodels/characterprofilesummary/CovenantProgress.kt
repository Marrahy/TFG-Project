package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary

data class CovenantProgress(
    val chosen_covenant: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.ChosenCovenant,
    val renown_level: Int,
    val soulbinds: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Soulbinds
)