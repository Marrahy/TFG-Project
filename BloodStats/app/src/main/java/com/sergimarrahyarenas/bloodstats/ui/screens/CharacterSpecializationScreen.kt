package com.sergimarrahyarenas.bloodstats.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.common.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.common.DynamicButton
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun CharacterSpecializationScreen(
    navController: NavController,
    blizzardViewModel: BlizzardViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope,
    context: Context
) {
    val characterActiveSpecialization by blizzardViewModel.characterActiveSpecialization.observeAsState()
    val characterActiveClassSpells by blizzardViewModel.listOfClassSpells.observeAsState(emptyList())
    val characterActiveSpecSpells by blizzardViewModel.listOfSpecSpells.observeAsState(emptyList())
    val characterProfileSummary by blizzardViewModel.characterProfileSummary.observeAsState()

    CustomScaffold(
        navController = navController,
        googleAuthUiClient = googleAuthUiClient,
        coroutineScope = coroutineScope,
        context = context,
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "$characterActiveSpecialization",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(characterActiveClassSpells) { classSpell ->
                            SpellCard(
                                typeTalent = "Talentos de clase",
                                spellName = classSpell?.spell?.name,
                                description = classSpell?.description,
                                castTime = classSpell?.cast_time,
                                cooldown = classSpell?.cooldown,
                                range = classSpell?.range,
                                manaCost = classSpell?.power_cost
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Box {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(characterActiveSpecSpells) { specSpell ->
                            SpellCard(
                                typeTalent = "Talentos de especializaición",
                                spellName = specSpell?.spell?.name,
                                description = specSpell?.description,
                                castTime = specSpell?.cast_time,
                                cooldown = specSpell?.cooldown,
                                range = specSpell?.range,
                                manaCost = specSpell?.power_cost
                            )
                        }
                    }
                }

                DynamicButton(
                    currentScreen = Routes.CharacterSpecializationScreen.route,
                    navController = navController,
                    blizzardViewModel = blizzardViewModel,
                    characterProfileSummary = characterProfileSummary,
                    characterActiveSpecialization = characterActiveSpecialization
                )
            }
        }
    )
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
    modifier: Modifier = Modifier
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