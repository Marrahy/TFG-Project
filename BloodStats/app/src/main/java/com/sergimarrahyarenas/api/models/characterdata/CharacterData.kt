package com.sergimarrahyarenas.api.models.characterdata

data class CharacterData(
    val _links: Links,
    val achievement_points: Int,
    val achievements: Achievements,
    val achievements_statistics: AchievementsStatistics,
    val active_spec: ActiveSpec,
    val appearance: Appearance,
    val average_item_level: Int,
    val character_class: CharacterClass,
    val collections: Collections,
    val covenant_progress: CovenantProgress,
    val encounters: Encounters,
    val equipment: Equipment,
    val equipped_item_level: Int,
    val experience: Int,
    val faction: Faction,
    val gender: Gender,
    val guild: Guild,
    val id: Int,
    val last_login_timestamp: Long,
    val level: Int,
    val media: Media,
    val mythic_keystone_profile: MythicKeystoneProfile,
    val name: String,
    val name_search: String,
    val professions: Professions,
    val pvp_summary: PvpSummary,
    val quests: Quests,
    val race: Race,
    val realm: RealmX,
    val reputations: Reputations,
    val specializations: Specializations,
    val statistics: Statistics,
    val titles: Titles
)