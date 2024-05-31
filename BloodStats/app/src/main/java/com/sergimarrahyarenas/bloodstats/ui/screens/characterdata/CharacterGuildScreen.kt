package com.sergimarrahyarenas.bloodstats.ui.screens.characterdata

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.R
import com.sergimarrahyarenas.bloodstats.data.network.client.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.ui.components.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.components.DynamicButton
import com.sergimarrahyarenas.bloodstats.ui.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun CharacterGuildScreen(
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    navController: NavController,
    coroutineScope: CoroutineScope,
) {
    val characterGuild by blizzardViewModel.characterGuildRoster.observeAsState()
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
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item {
                        characterGuild?.guild?.let { Text(text = it.name) }
                    }

                    item {
                        Spacer(modifier = Modifier.padding(8.dp))
                    }

                    item {
                        if (characterGuild?.guild?.faction?.type == "ALLIANCE") {
                            Image(
                                painter = painterResource(id = R.drawable.alliancelogo),
                                contentDescription = "Alliance Logo"
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.hordelogo),
                                contentDescription = "Horde Logo"
                            )
                        }
                    }

                    item {
                        Text(text = stringResource(R.string.character_members_guild_text))
                    }

                    item {
                        Spacer(modifier = Modifier.padding(8.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.padding(8.dp))
                    }

                    if (characterGuild?.members == null) {
                        item {
                            Image(
                                painter = painterResource(id = R.drawable.frog),
                                contentDescription = "Frog",
                                modifier = Modifier
                                    .size(250.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.padding(16.dp))

                            Text(
                                text = stringResource(R.string.character_members_error_text),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    } else {
                        items(characterGuild?.members ?: emptyList()) { member ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                ClickableText(
                                    text = AnnotatedString(member.character.name),
                                    onClick = {
                                        blizzardViewModel.loadCharacterProfileSummaryEquipmentMedia(
                                            member.character.name,
                                            member.character.realm.slug
                                        )
                                        navController.navigate(route = Routes.CharacterEquipmentScreen.route)
                                    },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    item {
                        DynamicButton(
                            currentScreen = Routes.CharacterGuildScreen.route,
                            navController = navController,
                            blizzardViewModel = blizzardViewModel,
                            characterProfileSummary = characterProfileSummary,
                            characterActiveSpecialization = characterActiveSpecialization,
                            userViewModel = userViewModel
                        )

                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                }
            }
        )
    }
}
