package com.sergimarrahyarenas.bloodstats.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sergimarrahyarenas.api.AuthService

@Composable
fun MainScreen() {
    val authService = AuthService()
    var accessToken by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        try {
            val clientId = "286817623f474bcface004dc20313828"
            val clientSecret = "UCysV7ZsseanapD1lOfVg7FRTpCj2GXl"
            val token = authService.getAccessToken(clientId, clientSecret)
            accessToken = token
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    Column {
        Text(text = "Acces Token: $accessToken")
    }
}