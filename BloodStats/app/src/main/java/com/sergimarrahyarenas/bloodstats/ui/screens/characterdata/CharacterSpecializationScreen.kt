package com.sergimarrahyarenas.bloodstats.ui.screens.characterdata

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.R
import com.sergimarrahyarenas.bloodstats.data.network.client.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.ui.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.components.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.components.DynamicButton
import com.sergimarrahyarenas.bloodstats.ui.components.TitleScreen
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun CharacterSpecializationScreen(
    navController: NavController,
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope,
) {
    val characterActiveSpecialization by blizzardViewModel.characterActiveSpecialization.observeAsState()
    val characterActiveClassSpells by blizzardViewModel.listOfClassSpells.observeAsState(emptyList())
    val characterActiveSpecSpells by blizzardViewModel.listOfSpecSpells.observeAsState(emptyList())
    val characterProfileSummary by blizzardViewModel.characterProfileSummary.observeAsState()
    val preferences by userViewModel.userPreferences.observeAsState()

    var showClassTalent by remember { mutableStateOf(false) }
    var showSpecTalent by remember { mutableStateOf(false) }

    val darkTheme = preferences?.theme == "dark"


    BloodStatsTheme(darkTheme = darkTheme) {
        CustomScaffold(
            navController = navController,
            googleAuthUiClient = googleAuthUiClient,
            coroutineScope = coroutineScope,
            userViewModel = userViewModel,
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TitleScreen(title = stringResource(R.string.specialization_text))
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "${stringResource(R.string.specialization_text)}: $characterActiveSpecialization",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    DynamicButton(
                        currentScreen = Routes.CharacterSpecializationScreen.route,
                        navController = navController,
                        blizzardViewModel = blizzardViewModel,
                        characterProfileSummary = characterProfileSummary,
                        characterActiveSpecialization = characterActiveSpecialization,
                        userViewModel = userViewModel
                    )

                    Button(
                        onClick = {
                            if (showSpecTalent) {
                                showSpecTalent = false
                                showClassTalent = !showClassTalent
                            } else {
                                showClassTalent = !showClassTalent
                            }
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                        ) {
                        Text(text = stringResource(R.string.character_class_talents_text_button))
                    }
                    Button(
                        onClick = {
                            if (showClassTalent) {
                                showClassTalent = false
                                showSpecTalent = !showSpecTalent
                            } else {
                                showSpecTalent = !showSpecTalent
                            }
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                        ) {
                        Text(text = stringResource(R.string.character_spec_talents_text_button))
                    }

                    AnimatedVisibility(
                        visible = showClassTalent,
                        enter = slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth }
                        )
                    ) {
                        Box {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (showClassTalent) {
                                    items(characterActiveClassSpells) { classSpell ->
                                        SpellCard(
                                            typeTalent = stringResource(R.string.character_class_talent_text),
                                            spellName = classSpell?.spell?.name,
                                            description = classSpell?.description,
                                            castTime = classSpell?.cast_time,
                                            cooldown = classSpell?.cooldown,
                                            range = classSpell?.range,
                                            manaCost = classSpell?.power_cost,
                                            userViewModel = userViewModel
                                        )
                                    }
                                }
                            }
                        }
                    }
                    AnimatedVisibility(
                        visible = showSpecTalent,
                        enter = slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth }
                        )
                    ) {
                        Box {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(characterActiveSpecSpells) { specSpell ->
                                    SpellCard(
                                        typeTalent = stringResource(R.string.character_spec_talent_text),
                                        spellName = specSpell?.spell?.name,
                                        description = specSpell?.description,
                                        castTime = specSpell?.cast_time,
                                        cooldown = specSpell?.cooldown,
                                        range = specSpell?.range,
                                        manaCost = specSpell?.power_cost,
                                        userViewModel = userViewModel
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun SpellCard(
    typeTalent: String,
    spellName: String?,
    description: String?,
    castTime: String?,
    cooldown: String?,
    range: String?,
    manaCost: String?,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    val preferences by userViewModel.userPreferences.observeAsState()
    val darkTheme = preferences?.theme == "dark"

    BloodStatsTheme(darkTheme = darkTheme) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = typeTalent,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (spellName != null) {
                    Text(
                        text = spellName,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                if (manaCost != null) {
                    if (manaCost.isNotEmpty()) {
                        Text(
                            text = manaCost,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
                if (castTime != null) {
                    Text(
                        text = castTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                if (cooldown != null) {
                    if (cooldown.isNotEmpty()) {
                        Text(
                            text = cooldown,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
                if (range != null) {
                    if (range.isNotEmpty()) {
                        Text(
                            text = range,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}