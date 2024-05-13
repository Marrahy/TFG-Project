package com.sergimarrahyarenas.bloodstats.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(key1 = true) {
        delay(2500)
        navController.popBackStack()
        navController.navigate(Routes.LoginScreen.route)
    }

    Splash()
}

@Composable
fun Splash() {
    Column {
        Text(text = "Splash Screen")
    }
}