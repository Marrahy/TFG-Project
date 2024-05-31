package com.sergimarrahyarenas.bloodstats.ui.components

import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.model.characterprofilesummary.CharacterProfileSummary
import com.sergimarrahyarenas.bloodstats.ui.navigation.Routes
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel

val allScreens = listOf(
    Routes.CharacterEquipmentScreen.route,
    Routes.CharacterGuildScreen.route,
    Routes.CharacterSpecializationScreen.route,
    Routes.CharacterStatisticsScreen.route,
    Routes.CharacterDungeonsScreen.route
)

object ButtonNavigationManagement {
    fun handleNavigation(
        screen: String,
        navController: NavController,
        blizzardViewModel: BlizzardViewModel,
        characterProfileSummary: CharacterProfileSummary?,
        characterActiveSpecialization: String?,
    ) {
        when (screen) {
            Routes.CharacterEquipmentScreen.route -> navController.navigate(route = Routes.CharacterEquipmentScreen.route)
            Routes.CharacterGuildScreen.route -> navController.navigate(route = Routes.CharacterGuildScreen.route)
            Routes.CharacterSpecializationScreen.route -> {
                if (characterActiveSpecialization == null) {
                    characterProfileSummary?.name?.let {
                        blizzardViewModel.loadCharacterSpecialization(
                            it, characterProfileSummary.realm.slug
                        )
                    }
                    navController.navigate(route = Routes.CharacterSpecializationScreen.route)
                } else {
                    navController.navigate(route = Routes.CharacterSpecializationScreen.route)
                }
            }
            Routes.CharacterDungeonsScreen.route -> navController.navigate(route = Routes.CharacterDungeonsScreen.route)
            Routes.CharacterStatisticsScreen.route -> navController.navigate(route = Routes.CharacterStatisticsScreen.route)
        }
    }
}