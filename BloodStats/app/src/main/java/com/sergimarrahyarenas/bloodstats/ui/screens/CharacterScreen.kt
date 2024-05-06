package com.sergimarrahyarenas.bloodstats.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sergimarrahyarenas.bloodstats.api.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.models.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.models.itemdata.ItemData
import com.sergimarrahyarenas.bloodstats.models.itemmedia.ItemMedia

@Composable
fun CharacterScreen(
    blizzardViewModel: BlizzardViewModel,
    navController: NavController
) {
    val characterInfo by blizzardViewModel.characterData.observeAsState(initial = null)
    val characterMedia by blizzardViewModel.mediaCharacter.observeAsState(initial = null)
    var listViewChecked by remember {
        mutableStateOf(false)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        GridListSwitch(
            listViewChecked = listViewChecked,
            onValueChange = { listViewChecked = it }
        ) {
            if (listViewChecked) {

            }
        }
    }

}

@Composable
fun GridListSwitch(
    listViewChecked: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Ver como: Cuadrícula")
        Switch(
            checked = listViewChecked,
            onCheckedChange = { onValueChange(it) },
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(text = "Lista")
    }
}

@Composable
fun EntitiesVerticalGrid(item: ItemMedia, items: List<ItemData>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items) { item ->

        }
    }
}

@Composable
fun ItemInfoGreed(item: ItemMedia, itemInfo: ItemData) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.padding(4.dp)
    ) {
        AsyncImage(
            model = item.assets[0].value,
            contentDescription = "Item Image",
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "${itemInfo.results}",
            fontSize = 12.sp,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        )
    }
}

@Composable
fun CharacterInfoGreed(character: CharacterMedia?) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.padding(4.dp)
    ) {
        AsyncImage(
            model = character?.assets?.get(1),
            contentDescription = "Character Image",
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "${character?.character?.name}",
            fontSize = 12.sp,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        )
    }
}