package com.sergimarrahyarenas.bloodstats.ui.screens.characterdata

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sergimarrahyarenas.bloodstats.R
import com.sergimarrahyarenas.bloodstats.data.network.client.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterstatistics.CharacterStatistics
import com.sergimarrahyarenas.bloodstats.ui.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.components.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.components.DynamicButton
import com.sergimarrahyarenas.bloodstats.ui.components.TitleScreen
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CharacterStatisticsScreen(
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope,
    context: Context
) {
    val characterProfileSummary by blizzardViewModel.characterProfileSummary.observeAsState()
    val characterActiveSpecialization by blizzardViewModel.characterActiveSpecialization.observeAsState()
    val characterStatistics by blizzardViewModel.characterStatistics.observeAsState()
    val characterMedia by blizzardViewModel.characterMedia.observeAsState()
    val characterMythicKeystone by blizzardViewModel.characterMythicKeystoneProfile.observeAsState()
    val isFavorite by userViewModel.isFavorite.observeAsState(initial = false)
    val user by userViewModel.user.observeAsState()
    val favorites by userViewModel.userFavorites.observeAsState()
    val preferences by userViewModel.userPreferences.observeAsState()

    val darkTheme = preferences?.theme == "dark"

    LaunchedEffect(key1 = true) {
        favorites?.forEach { favorite ->
            user.let {
                if (it != null) {
                    userViewModel.checkIfFavorite(it, favorite.characterName)
                }
            }
        }
    }

    BloodStatsTheme(darkTheme = darkTheme) {
        CustomScaffold(
            navController = navController,
            googleAuthUiClient = googleAuthUiClient,
            userViewModel = userViewModel,
            coroutineScope = coroutineScope,
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TitleScreen(title = stringResource(R.string.attributes_text))
                }

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    item {
                        AsyncImage(
                            model = characterMedia?.assets?.last()?.value,
                            contentDescription = "Character Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(256.dp)
                                .clip(CircleShape)
                        )
                    }

                    item {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.main_attributes_text),
                                    style = MaterialTheme.typography.titleLarge,
                                )

                                IconButton(
                                    onClick = {
                                        user?.let { user ->
                                            val characterName =
                                                characterStatistics?.character?.name ?: ""
                                            val characterRealmSlug =
                                                characterStatistics?.character?.realm?.slug ?: ""
                                            val characterMythicRating =
                                                characterMythicKeystone?.current_mythic_rating?.rating
                                                    ?: 0

                                            coroutineScope.launch {
                                                if (isFavorite) {
                                                    user.userUUID.let {
                                                        userViewModel.removeFavorite(
                                                            it,
                                                            characterName
                                                        )
                                                    }
                                                    withContext(Dispatchers.Main) {
                                                        Toast.makeText(
                                                            context,
                                                            context.getString(R.string.character_deleted_from_user_favorite_list_text),
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                } else {
                                                    user.userUUID.let {
                                                        userViewModel.addFavorite(
                                                            userUUID = it,
                                                            characterName = characterName,
                                                            characterRealmSlug = characterRealmSlug,
                                                            characterMythicRating = characterMythicRating.toInt()
                                                        )
                                                    }
                                                    withContext(Dispatchers.Main) {
                                                        Toast.makeText(
                                                            context,
                                                            context.getString(R.string.character_added_to_user_favorite_list_text),
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                        contentDescription = "Favorite Icon",
                                        modifier = Modifier.size(48.dp),
                                        tint = Color.Yellow
                                    )
                                }

                                if (characterStatistics != null) {
                                    Stats(
                                        characterStatistics = characterStatistics,
                                        blizzardViewModel = blizzardViewModel,
                                        userViewModel = userViewModel
                                    )
                                } else {
                                    CircularProgressIndicator(
                                        modifier = Modifier.padding(64.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        DynamicButton(
                            currentScreen = Routes.CharacterStatisticsScreen.route,
                            navController = navController,
                            blizzardViewModel = blizzardViewModel,
                            characterProfileSummary = characterProfileSummary,
                            characterActiveSpecialization = characterActiveSpecialization,
                            userViewModel = userViewModel
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun Stats(
    characterStatistics: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterstatistics.CharacterStatistics?,
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel
) {
    val preferences by userViewModel.userPreferences.observeAsState()
    val darkTheme = preferences?.theme == "dark"

    val stat by blizzardViewModel.characterPrimaryStat.observeAsState()

    val statText: Int? = when (stat) {
        R.string.intellect_text -> characterStatistics?.intellect?.effective
        R.string.strength_text -> characterStatistics?.strength?.effective
        R.string.agility_text -> characterStatistics?.agility?.effective
        else -> 0
    }

    BloodStatsTheme(darkTheme = darkTheme) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.padding(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatCard(attribute = stringResource(R.string.heart_points_text), value = "${characterStatistics?.health}")
                StatCard(attribute = stringResource(R.string.critical_chance_text), value = "${characterStatistics?.melee_crit?.value?.toInt()}")
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatCard(attribute = "${characterStatistics?.power_type?.name}", value = "${characterStatistics?.power}")
                StatCard(attribute = stringResource(R.string.haste_text), value = "${characterStatistics?.melee_haste?.value?.toInt()}")
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatCard(attribute = "${blizzardViewModel.characterPrimaryStat.value?.let { stringResource(id = it) }}", value = "$statText")
                StatCard(attribute = stringResource(R.string.mastery_text), value = "${characterStatistics?.mastery?.value?.toInt()}")
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatCard(attribute = stringResource(R.string.stamina_text), value = "${characterStatistics?.stamina?.effective}")
                StatCard(attribute = stringResource(R.string.versatility_text), value = "${characterStatistics?.versatility_damage_done_bonus?.toInt()}")
            }
        }
    }
}

@Composable
fun StatCard(attribute: String, value: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "$attribute: $value",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}