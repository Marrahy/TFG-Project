package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary

data class CharacterProfileSummary(
    val _links: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Links,
    val achievement_points: Int,
    val achievements: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Achievements,
    val achievements_statistics: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.AchievementsStatistics,
    val active_spec: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.ActiveSpec,
    val appearance: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Appearance,
    val average_item_level: Int,
    val character_class: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.CharacterClass,
    val collections: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Collections,
    val covenant_progress: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.CovenantProgress,
    val encounters: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Encounters,
    val equipment: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Equipment,
    val equipped_item_level: Int,
    val experience: Int,
    val faction: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Faction,
    val gender: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Gender,
    val guild: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Guild,
    val id: Int,
    val last_login_timestamp: Long,
    val level: Int,
    val media: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Media,
    val mythic_keystone_profile: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.MythicKeystoneProfile,
    val name: String,
    val name_search: String,
    val professions: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Professions,
    val pvp_summary: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.PvpSummary,
    val quests: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Quests,
    val race: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Race,
    val realm: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.RealmX,
    val reputations: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Reputations,
    val specializations: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Specializations,
    val statistics: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Statistics,
    val titles: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.Titles
)