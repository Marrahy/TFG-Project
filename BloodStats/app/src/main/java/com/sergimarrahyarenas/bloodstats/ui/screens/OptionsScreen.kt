package com.sergimarrahyarenas.bloodstats.ui.screens

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.navigation.Routes

@Composable
fun OptionsScreen(navController: NavController) {
    Button(onClick = {navController.navigate(route = Routes.SearchScreen.route)}) {

    }
}