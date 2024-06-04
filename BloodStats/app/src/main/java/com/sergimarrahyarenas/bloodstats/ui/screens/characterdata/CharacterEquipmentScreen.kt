package com.sergimarrahyarenas.bloodstats.ui.screens.characterdata

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sergimarrahyarenas.bloodstats.R
import com.sergimarrahyarenas.bloodstats.data.network.client.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.EquippedItem
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemmedia.ItemMedia
import com.sergimarrahyarenas.bloodstats.ui.components.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.components.DynamicButton
import com.sergimarrahyarenas.bloodstats.ui.components.TitleScreen
import com.sergimarrahyarenas.bloodstats.ui.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.screens.itemdata.getColorByQuality
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@Composable
fun CharacterEquipmentScreen(
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope,
) {
    val preferences by userViewModel.userPreferences.observeAsState()

    val darkTheme = preferences?.theme == "dark"

    BloodStatsTheme(darkTheme = darkTheme) {
        CustomScaffold(
            navController = navController,
            googleAuthUiClient = googleAuthUiClient,
            userViewModel = userViewModel,
            coroutineScope = coroutineScope,
            content = {
                val characterProfileSummary by blizzardViewModel.characterProfileSummary.observeAsState()
                val characterMedia by blizzardViewModel.characterMedia.observeAsState()
                val characterEquipment by blizzardViewModel.equippedItems.observeAsState()
                val characterEquipmentMedia by blizzardViewModel.equippedItemsMedia.observeAsState()
                val characterActiveSpecialization by blizzardViewModel.characterActiveSpecialization.observeAsState()

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

                    Spacer(modifier = Modifier.padding(8.dp))

                    DynamicButton(
                        currentScreen = Routes.CharacterEquipmentScreen.route,
                        navController = navController,
                        blizzardViewModel = blizzardViewModel,
                        characterProfileSummary = characterProfileSummary,
                        characterActiveSpecialization = characterActiveSpecialization,
                        userViewModel = userViewModel
                    )

                    if (characterEquipment?.isEmpty() == true) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
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
    item: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.EquippedItem?,
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