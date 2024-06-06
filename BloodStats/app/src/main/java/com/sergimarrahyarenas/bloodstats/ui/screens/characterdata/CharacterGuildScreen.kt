package com.sergimarrahyarenas.bloodstats.ui.screens.characterdata

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.sergimarrahyarenas.bloodstats.ui.components.TitleScreen
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

    var showDialog by remember { mutableStateOf(false) }
    var hasCheckedGuild by remember { mutableStateOf(false) }

    val darkTheme = preferences?.theme == "dark"

    BloodStatsTheme(darkTheme = darkTheme) {
        CustomScaffold(
            navController = navController,
            googleAuthUiClient = googleAuthUiClient,
            userViewModel = userViewModel,
            coroutineScope = coroutineScope,
            content = {
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDialog = false
                                    navController.popBackStack()
                                }
                            ) {
                                Text(text = stringResource(R.string.ok_text))
                            }
                        },
                        title = {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = stringResource(R.string.character_guild_error_text))

                                Spacer(modifier = Modifier.padding(8.dp))

                                Icon(
                                    imageVector = Icons.Default.WarningAmber,
                                    contentDescription = "Warning",
                                    tint = Color.Red,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        },
                        text = {
                            Text(text = stringResource(R.string.character_members_error_text))
                        }
                    )
                }

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TitleScreen(title = stringResource(R.string.guild_text))
                        }
                    }
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
                        Spacer(modifier = Modifier.padding(16.dp))
                    }

                    if (characterGuild?.members == null && !hasCheckedGuild) {
                        item {
                            LaunchedEffect(Unit) {
                                showDialog = true
                                hasCheckedGuild = true
                            }
                            Image(
                                painter = painterResource(id = R.drawable.frog),
                                contentDescription = "Frog",
                                modifier = Modifier
                                    .size(250.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
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
