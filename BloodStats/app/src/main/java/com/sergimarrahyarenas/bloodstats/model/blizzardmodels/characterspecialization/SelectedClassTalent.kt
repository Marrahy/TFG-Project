package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization

data class SelectedClassTalent(
    val default_points: Int,
    val id: Int,
    val rank: Int,
    val tooltip: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.Tooltip
)