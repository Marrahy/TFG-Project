package com.sergimarrahyarenas.bloodstats.model.characterspecialization

data class CharacterSpecialization(
    val _links: Links,
    val active_specialization: ActiveSpecialization,
    val character: Character,
    val specializations: List<Specialization>
)