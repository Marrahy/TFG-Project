package com.sergimarrahyarenas.bloodstats.ui.navigation

sealed class Routes(val route: String) {
    object SplashScreen : Routes("splash_screen")
    object LoadingScreen : Routes("loading_screen")
    object LoginScreen : Routes("login_screen")
    object OptionsScreen : Routes("options_screen")
    object SearchScreen : Routes("search_screen")
    object ProfileScreen : Routes("profile_screen")
    object CharacterStatisticsScreen : Routes("character_statistics_screen")
    object CharacterEquipmentScreen : Routes("character_equipment_screen")
    object CharacterSpecializationScreen : Routes("character_specialization_screen")
    object CharacterDungeonsScreen : Routes("character_dungeons_screen")
    object CharacterGuildScreen : Routes("character_guild_screen")
    object ItemDataScreen : Routes("item_data_screen")
}