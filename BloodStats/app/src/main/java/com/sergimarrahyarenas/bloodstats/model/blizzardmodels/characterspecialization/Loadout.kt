package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization

data class Loadout(
    val is_active: Boolean,
    val selected_class_talents: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.SelectedClassTalent>,
    val selected_spec_talents: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.SelectedSpecTalent>,
    val talent_loadout_code: String
)