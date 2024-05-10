package com.sergimarrahyarenas.bloodstats.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sergimarrahyarenas.bloodstats.api.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.common.CustomScaffold
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.presentation.sign_in.GoogleAuthUiClient
import kotlinx.coroutines.CoroutineScope

@Composable
fun CharacterEquipmentScreen(
    blizzardViewModel: BlizzardViewModel,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope
) {


    CustomScaffold(
        navController = navController,
        googleAuthUiClient = googleAuthUiClient,
        coroutineScope = coroutineScope,
        content = {
            val characterProfileSummary by blizzardViewModel.characterProfileSummary.observeAsState()
            val characterMedia by blizzardViewModel.characterMedia.observeAsState()
            val characterEquipment by blizzardViewModel.characterEquipment.observeAsState()
            val characterEquipmentMedia by blizzardViewModel.characterEquipmentMedia.observeAsState()
            val buttonList = listOf(
                "Atributos", "Clan", "Especialización", "Mazmorras"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "${characterProfileSummary?.name}")
                    Text(text = "${characterProfileSummary?.character_class?.name} ${characterProfileSummary?.active_spec?.name}")
                    Text(text = "iLvL: ${characterProfileSummary?.equipped_item_level}")
                }
                Spacer(modifier = Modifier.padding(8.dp))
                AsyncImage(
                    model = characterMedia?.assets?.get(1)?.value,
                    contentDescription = "Character Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(width = 150.dp, height = 150.dp)
                )

                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    items(buttonList.size) { index ->
                        Button(
                            onClick = {
                                when(index) {
                                    0 -> {
                                        navController.navigate(route = Routes.CharacterStatisticsScreen.route)
                                        blizzardViewModel.loadCharacterStatistics(characterProfileSummary!!.name.toLowerCase(), characterProfileSummary!!.realm.name.toLowerCase())
                                    }
                                    1 -> navController.navigate(route = Routes.CharacterGuildScreen.route)
                                    2 -> navController.navigate(route = Routes.CharacterSpecializationScreen.route)
                                    3 -> navController.navigate(route = Routes.CharacterDungeonsScreen.route)
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

