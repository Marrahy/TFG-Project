package com.sergimarrahyarenas.bloodstats.model.characterspecialization

data class Specialization(
    val glyphs: List<Glyph>,
    val loadouts: List<Loadout>,
    val pvp_talent_slots: List<PvpTalentSlot>,
    val specialization: SpecializationX
)