package com.sergimarrahyarenas.bloodstats.model.characterspecialization

data class Loadout(
    val is_active: Boolean,
    val selected_class_talents: List<SelectedClassTalent>,
    val selected_spec_talents: List<SelectedSpecTalent>,
    val talent_loadout_code: String
)