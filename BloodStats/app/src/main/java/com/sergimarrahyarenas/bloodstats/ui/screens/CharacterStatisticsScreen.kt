package com.sergimarrahyarenas.bloodstats.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.models.characterstatistics.CharacterStatistics
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import com.sergimarrahyarenas.bloodstats.preferences.SharedPreferencesUtils
import com.sergimarrahyarenas.bloodstats.ui.common.ButtonNavigationManagement
import com.sergimarrahyarenas.bloodstats.ui.common.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.common.DynamicButton
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
    val characterGuildRoster by blizzardViewModel.characterGuildRoster.observeAsState()
    val characterStatistics by blizzardViewModel.characterStatistics.observeAsState()
    val characterMedia by blizzardViewModel.characterMedia.observeAsState()
    val characterMythicKeystoneProfile by blizzardViewModel.characterMythicKeystoneProfile.observeAsState()
    val isFavorite by userViewModel.isFavorite.observeAsState(initial = false)

    val userUUID = SharedPreferencesUtils.getUserUUID(context)

    LaunchedEffect(characterStatistics, userUUID) {
        userUUID?.let {
            val characterName = characterStatistics?.character?.name ?: ""
            val characterRealmSlug = characterStatistics?.character?.realm?.slug ?: ""
            userViewModel.checkIfFavorite(characterName, characterRealmSlug)
        }
    }

    CustomScaffold(
        navController = navController,
        googleAuthUiClient = googleAuthUiClient,
        coroutineScope = coroutineScope,
        context = context,
        content = {
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

                IconButton(
                    onClick = {
                        userUUID?.let { userUUID ->
                            val characterName = characterStatistics?.character?.name ?: ""
                            val characterRealmSlug =
                                characterStatistics?.character?.realm?.slug ?: ""
                            val characterMythicRating =
                                characterMythicKeystoneProfile?.current_mythic_rating?.rating ?: 0

                            coroutineScope.launch {
                                if (isFavorite) {
                                    userViewModel.removeFavorite(
                                        userUUID = userUUID,
                                        characterName = characterName,
                                        characterRealmSlug = characterRealmSlug,
                                    )
                                    withContext(Dispatchers.Main) {
                                        Toast
                                            .makeText(
                                                context,
                                                "Eliminado de la lista de favoritos!",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                } else {
                                    userViewModel.addFavorite(
                                        userUUID = userUUID,
                                        characterName = characterName,
                                        characterRealmSlug = characterRealmSlug,
                                        characterMythicRating = characterMythicRating.toInt()
                                    )
                                    withContext(Dispatchers.Main) {
                                        Toast
                                            .makeText(
                                                context,
                                                "Agregado a la lista de favoritos!",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                        contentDescription = "Favorite Icon",
                        modifier = Modifier
                            .size(48.dp),
                        tint = Color.Yellow
                    )
                }

                Stats(
                    characterStatistics = characterStatistics,
                    blizzardViewModel = blizzardViewModel
                )

                DynamicButton(
                    currentScreen = Routes.CharacterStatisticsScreen.route,
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
fun Stats(
    characterStatistics: CharacterStatistics?,
    blizzardViewModel: BlizzardViewModel
) {
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
            Text(text = "${blizzardViewModel.characterPrimaryStat.value}: ${characterStatistics?.strength?.effective}")
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