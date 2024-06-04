package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization

data class Specialization(
    val glyphs: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.Glyph>,
    val loadouts: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.Loadout>,
    val pvp_talent_slots: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.PvpTalentSlot>,
    val specialization: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.SpecializationX
)