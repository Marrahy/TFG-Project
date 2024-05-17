package com.sergimarrahyarenas.bloodstats.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel

@Composable
fun CharacterSpecializationScreen(
    navController: NavController,
    blizzardViewModel: BlizzardViewModel,
    googleAuthUiClient: GoogleAuthUiClient
) {
    val characterSpecialization by blizzardViewModel.characterSpecialization.observeAsState()

}