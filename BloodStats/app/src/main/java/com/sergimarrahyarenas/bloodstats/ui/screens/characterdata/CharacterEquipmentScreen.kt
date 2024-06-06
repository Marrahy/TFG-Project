package com.sergimarrahyarenas.bloodstats.ui.screens.characterdata

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.EquippedItem
import com.sergimarrahyarenas.bloodstats.ui.components.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.components.DynamicButton
import com.sergimarrahyarenas.bloodstats.ui.components.TitleScreen
import com.sergimarrahyarenas.bloodstats.ui.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CharacterEquipmentScreen(
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope,
    context: Context
) {
    val preferences by userViewModel.userPreferences.observeAsState()
    val favorites by userViewModel.userFavorites.observeAsState()
    val user by userViewModel.user.observeAsState()
    val characterProfileSummary by blizzardViewModel.characterProfileSummary.observeAsState()

    val darkTheme = preferences?.theme == "dark"

    favorites?.forEach { _ ->
        user?.let {
            characterProfileSummary?.name?.let { characterName ->
                userViewModel.checkIfFavorite(
                    it,
                    characterName
                )
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
                val characterMedia by blizzardViewModel.characterMedia.observeAsState()
                val characterEquipment by blizzardViewModel.equippedItems.observeAsState()
                val characterEquipmentMedia by blizzardViewModel.equippedItemsMedia.observeAsState()
                val characterActiveSpecialization by blizzardViewModel.characterActiveSpecialization.observeAsState()
                val characterStatistics by blizzardViewModel.characterStatistics.observeAsState()
                val characterMythicKeystone by blizzardViewModel.characterMythicKeystoneProfile.observeAsState()
                val isFavorite by userViewModel.isFavorite.observeAsState(initial = false)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    TitleScreen(title = stringResource(R.string.equipment_text))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "${characterProfileSummary?.character_class?.name} ${characterProfileSummary?.active_spec?.name}")
                        Text(text = "iLvL: ${characterProfileSummary?.equipped_item_level}")
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = characterMedia?.assets?.get(1)?.value,
                                contentDescription = "Character Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(width = 150.dp, height = 150.dp)
                            )

                            Spacer(modifier = Modifier.padding(16.dp))

                            Text(
                                text = "${characterProfileSummary?.name}",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Card(
                            modifier = Modifier
                                .padding(8.dp),
                            shape = MaterialTheme.shapes.medium,
                            elevation = CardDefaults.cardElevation(4.dp)

                        ) {
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
                        }
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    DynamicButton(
                        currentScreen = Routes.CharacterEquipmentScreen.route,
                        navController = navController,
                        blizzardViewModel = blizzardViewModel,
                        characterProfileSummary = characterProfileSummary,
                        characterActiveSpecialization = characterActiveSpecialization,
                        userViewModel = userViewModel
                    )

                    if (characterEquipmentMedia == null) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(text = "Cargando...")
                            CircularProgressIndicator(
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        characterEquipment?.let { equipment ->
                            characterEquipmentMedia?.let { equipmentMedia ->
                                CharacterEquipmentList(
                                    navController = navController,
                                    characterEquipment = equipment,
                                    characterEquipmentMedia = equipmentMedia,
                                    blizzardViewModel = blizzardViewModel,
                                    userViewModel = userViewModel
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun EquipmentItemCard(
    item: EquippedItem?,
    itemMediaUrl: String?,
    navController: NavController,
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel
) {
    val preferences by userViewModel.userPreferences.observeAsState()
    val itemData by blizzardViewModel.itemData.observeAsState()

    val darkTheme = preferences?.theme == "dark"

    BloodStatsTheme(darkTheme = darkTheme) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    if (itemData == null) {
                        item?.item?.id?.let { blizzardViewModel.loadItemDataAndMedia(it) }
                        navController.navigate(route = Routes.ItemDataScreen.route)
                    } else {
                        navController.navigate(route = Routes.ItemDataScreen.route)
                    }
                },
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(16.dp)
            ) {
                AsyncImage(
                    model = itemMediaUrl,
                    contentDescription = "Item Media",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                item?.name?.let { Text(text = it) }
            }
        }
    }
}

@Composable
fun CharacterEquipmentList(
    navController: NavController,
    characterEquipment: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.EquippedItem?>,
    characterEquipmentMedia: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemmedia.ItemMedia?>,
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel
) {
    val preferences by userViewModel.userPreferences.observeAsState()

    val darkTheme = preferences?.theme == "dark"

    BloodStatsTheme(darkTheme = darkTheme) {
        LazyColumn(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            items(characterEquipment.size) { item ->
                val itemEquipped = characterEquipment[item]
                val itemMediaUrl = characterEquipmentMedia.getOrNull(item)

                if (itemEquipped != null) {
                    EquipmentItemCard(
                        item = itemEquipped,
                        itemMediaUrl = itemMediaUrl?.assets?.get(0)?.value,
                        navController = navController,
                        blizzardViewModel = blizzardViewModel,
                        userViewModel = userViewModel
                    )
                }
            }
        }
    }
}