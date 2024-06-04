package com.sergimarrahyarenas.bloodstats.ui.screens.itemdata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.ItemData
import com.sergimarrahyarenas.bloodstats.ui.components.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun ItemDataScreen(
    navController: NavController,
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope,
) {
    val itemData by blizzardViewModel.itemData.observeAsState()
    val preferences by userViewModel.userPreferences.observeAsState()

    val darkTheme = preferences?.theme == "dark"


    BloodStatsTheme(darkTheme = darkTheme) {
        CustomScaffold(
            navController = navController,
            googleAuthUiClient = googleAuthUiClient,
            userViewModel = userViewModel,
            coroutineScope = coroutineScope,
            content = {
                Box(modifier = Modifier.fillMaxSize()) {
                    itemData?.let { item ->
                        LazyColumn(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 64.dp)
                        ) {
                            item {
                                ItemDetail(
                                    item = item,
                                    blizzardViewModel = blizzardViewModel,
                                    userViewModel = userViewModel
                                )
                            }
                        }
                    } ?: run {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .padding(64.dp)
                                )
                                Spacer(modifier = Modifier.padding(8.dp))
                                Text(text = stringResource(R.string.loading_text))
                            }
                        }
                    }

                    Button(
                        onClick = {
                            blizzardViewModel.clearItemData()
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Arrow Back"
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun ItemDetail(
    item: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.ItemData,
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel
) {
    val itemMedia by blizzardViewModel.itemMedia.observeAsState()
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = getColorByQuality(item.quality.type)
                )

                Spacer(modifier = Modifier.padding(8.dp))

                AsyncImage(
                    model = itemMedia?.assets?.get(0)?.value,
                    contentDescription = "Item Media",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.preview_item.level.display_string,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = item.preview_item.inventory_type.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                item.preview_item.binding?.name?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                item.preview_item?.stats?.forEach { stat ->
                    Text(
                        text = stat.display.display_string,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }

                item.preview_item?.sell_price?.let { sellPrice ->
                    Text(
                        text = sellPrice.display_strings.header,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Text(
                        text = "${sellPrice.display_strings.gold} Gold ${sellPrice.display_strings.silver} Silver ${sellPrice.display_strings.copper} Copper",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                if (item.preview_item?.requirements?.level != null) {
                    item.preview_item.requirements.level.let { requirement ->
                        Text(
                            text = requirement.display_string,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                if (item.preview_item?.durability != null) {
                    item.preview_item.durability.let { durability ->
                        Text(
                            text = durability.display_string,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                if (item.preview_item?.set != null) {
                    item.preview_item.set.let { itemSet ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = itemSet.display_string,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )

                        itemSet.items.forEach { setItem ->
                            Text(
                                text = setItem.item.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        itemSet.effects.forEach { effect ->
                            Text(
                                text = effect.display_string,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getColorByQuality(quality: String): Color {
    return when (quality) {
        "UNCOMMON" -> Color(30, 255, 0)
        "RARE" -> Color(0, 112, 221)
        "EPIC" -> Color(163, 53, 238)
        "LEGENDARY" -> Color(255, 128, 0)
        "ARTIFACT" -> Color(230, 204, 128)
        "HEIRLOOM" -> Color(0, 204, 255)
        else -> MaterialTheme.colorScheme.secondary
    }
}