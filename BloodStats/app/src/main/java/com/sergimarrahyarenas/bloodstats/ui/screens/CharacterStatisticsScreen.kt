package com.sergimarrahyarenas.bloodstats.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.common.CustomScaffold
import com.sergimarrahyarenas.bloodstats.models.characterstatistics.CharacterStatistics
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import kotlinx.coroutines.CoroutineScope

@Composable
fun CharacterStatisticsScreen(
    blizzardViewModel: BlizzardViewModel,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope
) {
    val characterStatistics by blizzardViewModel.characterStatistics.observeAsState()
    val characterMedia by blizzardViewModel.characterMedia.observeAsState()
    val buttonList = listOf(
        "Equipamiento", "Clan", "Especialización", "Mazmorras"
    )
    CustomScaffold(
        navController = navController,
        googleAuthUiClient = googleAuthUiClient,
        coroutineScope = coroutineScope,
        content =  {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImage(
                    model = characterMedia?.assets?.last()?.value,
                    contentDescription = "Character Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape)
                )
                Text(text = "Atributos Principales")

                Stats(characterStatistics = characterStatistics, blizzardViewModel = blizzardViewModel)

                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    items(buttonList.size) { index ->
                        Button(
                            onClick = {
                                when(index) {
                                    0 -> navController.navigate(route = Routes.CharacterEquipmentScreen.route)
                                }
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(text = buttonList[index])
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun Stats(characterStatistics: CharacterStatistics?, blizzardViewModel: BlizzardViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.padding(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Salud: ${characterStatistics?.health}")
            Text(text = "Golpe Crítico: ${characterStatistics?.melee_crit?.value?.toInt()}")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "${characterStatistics?.power_type?.name}: ${characterStatistics?.power}")
            Text(text = "Celeridad: ${characterStatistics?.melee_haste?.value?.toInt()}")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "${blizzardViewModel.characterSpec.value}: ${characterStatistics?.strength?.effective}")
            Text(text = "Maestría: ${characterStatistics?.mastery?.value?.toInt()}")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Aguante: ${characterStatistics?.stamina?.effective}")
            Text(text = "Versatilidad: ${characterStatistics?.versatility_damage_done_bonus?.toInt()}")
        }
    }
}