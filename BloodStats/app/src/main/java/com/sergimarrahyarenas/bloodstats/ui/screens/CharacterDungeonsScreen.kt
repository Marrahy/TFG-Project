package com.sergimarrahyarenas.bloodstats.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.common.DynamicButton
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel

@Composable
fun CharacterDungeonsScreen(
    navController: NavController,
    blizzardViewModel: BlizzardViewModel
) {
    val characterProfileSummary by blizzardViewModel.characterProfileSummary.observeAsState()
    val characterActiveSpecialization by blizzardViewModel.characterActiveSpecialization.observeAsState()

    DynamicButton(
        currentScreen = Routes.CharacterDungeonsScreen.route,
        navController = navController,
        blizzardViewModel = blizzardViewModel,
        characterProfileSummary = characterProfileSummary,
        characterActiveSpecialization = characterActiveSpecialization
    )
}