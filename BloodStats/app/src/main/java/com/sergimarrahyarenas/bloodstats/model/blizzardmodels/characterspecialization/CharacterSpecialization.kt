package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization

data class CharacterSpecialization(
    val _links: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.Links,
    val active_specialization: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.ActiveSpecialization,
    val character: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.Character,
    val specializations: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.Specialization>
)