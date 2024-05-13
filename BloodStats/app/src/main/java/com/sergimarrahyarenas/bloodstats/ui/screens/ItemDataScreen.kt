package com.sergimarrahyarenas.bloodstats.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.common.CustomScaffold
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun ItemDataScreen(
    navController: NavController,
    blizzardViewModel: BlizzardViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope
) {
    val itemData by blizzardViewModel.itemData.observeAsState()
    val itemMedia by blizzardViewModel.itemMedia.observeAsState()
    val itemStats by blizzardViewModel.itemStats.observeAsState()

    CustomScaffold(
        navController = navController,
        googleAuthUiClient = googleAuthUiClient,
        coroutineScope = coroutineScope,
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = """Quality: ${itemData?.quality?.name}
                    |${itemData?.preview_item?.requirements?.level?.display_string}
                    |Item Inventory type: ${itemData?.inventory_type?.name}
                    |Item Name: ${itemData?.name}
                    |Item Biding: ${itemData?.preview_item?.binding}
                    |Item Armor: ${itemData?.preview_item?.armor?.display?.display_string}
                    |Item Stats: ${itemStats?.get(0)?.name}: ${itemStats?.get(0)?.display_string}
                                |${itemStats?.get(1)?.name}: ${itemStats?.get(1)?.display_string}
                                |${itemStats?.get(2)?.name}: ${itemStats?.get(2)?.display_string}
                                |${itemStats?.get(3)?.name}: ${itemStats?.get(3)?.display_string}
                    |Item Class: ${itemData?.preview_item?.requirements?.playable_classes?.display_string}
                """.trimMargin())
            }
        }
    )
}