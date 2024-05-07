package com.sergimarrahyarenas.bloodstats.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sergimarrahyarenas.bloodstats.api.viewmodel.BlizzardViewModel

@Composable
fun CharacterScreen(
    blizzardViewModel: BlizzardViewModel,
    navController: NavController,
) {
    val characterInfo by blizzardViewModel.characterData.observeAsState()
    val characterMedia by blizzardViewModel.characterMedia.observeAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = characterMedia?.assets?.get(2)?.value,
            contentDescription = "Character Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(CircleShape)
        )
        Row(

        ) {
            characterInfo?.name?.let {
                Text(
                    text = "Nombre: $it"
                )
            }
            characterInfo?.equipped_item_level?.let {
                Text(
                    text = "Nivel de equipo: $it"
                )
            }
            characterInfo?.character_class?.name?.let {
                Text(
                    text = "Clase: $it"
                )
            }
        }
    }
}

