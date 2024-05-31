package com.sergimarrahyarenas.bloodstats.ui.screens.characterdata

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.R
import com.sergimarrahyarenas.bloodstats.data.network.client.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.ui.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.components.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.components.DynamicButton
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun CharacterDungeonsScreen(
    navController: NavController,
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope,
) {
    val characterEncounters by blizzardViewModel.characterEncounters.observeAsState()
    val characterProfileSummary by blizzardViewModel.characterProfileSummary.observeAsState()
    val characterActiveSpecialization by blizzardViewModel.characterActiveSpecialization.observeAsState()
    val preferences by userViewModel.userPreferences.observeAsState()

    val darkTheme = preferences?.theme == "dark"

    BloodStatsTheme(darkTheme = darkTheme) {
        CustomScaffold(
            navController = navController,
            googleAuthUiClient = googleAuthUiClient,
            userViewModel = userViewModel,
            coroutineScope = coroutineScope,
            content = {
                val dungeonImageMap = mapOf(
                    "Algeth'ar Academy" to R.drawable.algethar_academy_small,
                    "Uldaman: Legacy of Tyr" to R.drawable.uldaman_legacy_of_tyr_small,
                    "The Nokhud Offensive" to R.drawable.the_nokhud_offensive_small,
                    "The Azure Vault" to R.drawable.the_azure_vault_small,
                    "Neltharus" to R.drawable.neltharus_small,
                    "Ruby Life Pools" to R.drawable.ruby_life_pools_small,
                    "Brackenhide Hollow" to R.drawable.brackenhide_hollow_small,
                    "Halls of Infusion" to R.drawable.halls_of_infusion_small
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        characterEncounters?.expansions?.find { it.expansion.name == "Dragonflight" }?.instances?.forEach { instance ->
                            instance.modes.find { it.difficulty.type == "MYTHIC" }?.let { mode ->
                                val dungeonImage = dungeonImageMap[instance.instance.name] ?: R.drawable.frog
                                mode.progress.encounters.forEach { encounter ->
                                    item {
                                        DungeonCard(
                                            instanceName = instance.instance.name,
                                            difficulty = mode.difficulty.name,
                                            status = mode.status.name,
                                            completedCount = encounter.completed_count,
                                            dungeonImage = dungeonImage,
                                            userViewModel = userViewModel
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.padding(16.dp))

                            DynamicButton(
                                currentScreen = Routes.CharacterDungeonsScreen.route,
                                navController = navController,
                                blizzardViewModel = blizzardViewModel,
                                characterProfileSummary = characterProfileSummary,
                                characterActiveSpecialization = characterActiveSpecialization,
                                userViewModel = userViewModel
                            )

                            Spacer(modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun DungeonCard(
    instanceName: String,
    difficulty: String,
    status: String,
    completedCount: Int,
    dungeonImage: Int,
    userViewModel: UserViewModel
) {
    val preferences by userViewModel.userPreferences.observeAsState()

    val darkTheme = preferences?.theme == "dark"

    BloodStatsTheme(darkTheme = darkTheme) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(MaterialTheme.shapes.medium),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = dungeonImage),
                    contentDescription = "Dungeon Image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(text = instanceName, style = MaterialTheme.typography.titleMedium)
                    Text(text = stringResource(R.string.dungeon_difficulty_text, difficulty), style = MaterialTheme.typography.bodyMedium)
                    Text(text = stringResource(R.string.dungeon_state_text, status), style = MaterialTheme.typography.bodyMedium)
                    Text(text = stringResource(R.string.character_count_number_text, completedCount), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}