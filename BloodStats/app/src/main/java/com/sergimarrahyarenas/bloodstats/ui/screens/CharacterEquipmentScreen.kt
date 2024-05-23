package com.sergimarrahyarenas.bloodstats.ui.screens

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.models.characterequipment.CharacterEquipment
import com.sergimarrahyarenas.bloodstats.models.characterequipment.EquippedItem
import com.sergimarrahyarenas.bloodstats.models.itemmedia.ItemMedia
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.common.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.common.DynamicButton
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun CharacterEquipmentScreen(
    blizzardViewModel: BlizzardViewModel,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope,
    context: Context
) {
    CustomScaffold(
        navController = navController,
        googleAuthUiClient = googleAuthUiClient,
        coroutineScope = coroutineScope,
        context = context,
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
                    characterActiveSpecialization = characterActiveSpecialization
                )
                    characterEquipment?.let { equipment ->
                        characterEquipmentMedia?.let { equipmentMedia ->
                            CharacterEquipmentList(
                                navController = navController,
                                characterEquipment = equipment,
                                characterEquipmentMedia = equipmentMedia
                            )
                        }
                    } ?: run {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(text = "Cargando Equipo...")
                }
            }
        }
    )
}

@Composable
fun EquipmentItemCard(
    item: EquippedItem,
    itemMediaUrl: String?,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate(route = Routes.ItemDataScreen.route)
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
                model = itemMediaUrl ?: "Cargando...",
                contentDescription = "Item Media",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = item.name)
        }
    }
}

@Composable
fun CharacterEquipmentList(
    navController: NavController,
    characterEquipment: List<EquippedItem?>,
    characterEquipmentMedia: List<ItemMedia?>
) {
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
                    navController = navController
                )
            }
        }
    }
}

