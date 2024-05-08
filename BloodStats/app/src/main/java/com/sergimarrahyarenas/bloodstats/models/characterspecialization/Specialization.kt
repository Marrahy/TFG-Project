package com.sergimarrahyarenas.bloodstats.models.characterspecialization

data class Specialization(
    val glyphs: List<Glyph>,
    val loadouts: List<Loadout>,
    val pvp_talent_slots: List<PvpTalentSlot>,
    val specialization: SpecializationX
)