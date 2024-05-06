package com.sergimarrahyarenas.bloodstats.navigation

sealed class Routes(val route: String) {
    object SplashScreen : Routes("splash_screen")
    object LoginScreen : Routes("login_screen")
    object RegisterScreen : Routes("register_screen")
    object MainScreen : Routes("main_screen")
    object LoadingScreen : Routes("loading_info")
    object ProfileScreen : Routes("profile_screen")
    object CharacterScreen : Routes("character_screen")
    object CharacterStatsScreen : Routes("character_stats_screen")
    object CharacterSkillTreeScreen : Routes("character_skill_tree_screen")
    object CharacterDungeonsScreen : Routes("character_dungeons_screen")
    object CharacterGuildScreen : Routes("character_guild_screen")
    object BuildPlannerScreen : Routes("build_planner_screen")
    object BossGeneralInfoScreen : Routes("boss_general_info_screen")
    object CreatureGeneralInfoScreen : Routes("creature_general_info_screen")
    object NpcGeneralInfoScreen : Routes("npc_general_info_screen")
}