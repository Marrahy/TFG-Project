package com.sergimarrahyarenas.bloodstats.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.api.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.models.itemdata.ItemData
import com.sergimarrahyarenas.bloodstats.models.itemmedia.ItemMedia

@Composable
fun MainScreen(
    blizzardViewModel: BlizzardViewModel,
    navController: NavController
) {
    val isLoading: Boolean by blizzardViewModel.isLoading.observeAsState(initial = false)
    val responseError: Boolean by blizzardViewModel.responseError.observeAsState(initial = false)

    var searchedEntity by remember {
        mutableStateOf("")
    }
    var realm by remember {
        mutableStateOf("")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SearchBox(
                modifier = Modifier.weight(1f),
                searchedEntity = searchedEntity,
                realm = realm,
                onValueChange = { searchedEntity = it },
                onClickPress = {
                    blizzardViewModel.loadCharacterDataAndMedia(searchedEntity, realm)
                    blizzardViewModel.loadItemDataAndMedia(searchedEntity)
                }
            )
        }

        Box(modifier = Modifier.weight(8f)) {
            if (isLoading) {
                navController.navigate("loading_screen")
            } else if (responseError) {
                Text(text = "\"$searchedEntity\" no coincide con ninguna busqueda")
            }
        }
    }
}

@Composable
fun SearchBox(
    modifier: Modifier = Modifier,
    searchedEntity: String,
    realm: String = "",
    onValueChange: (String) -> Unit,
    onClickPress: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .then(modifier)
        ) {
            OutlinedTextField(
                value = searchedEntity,
                onValueChange = { onValueChange(it) },
                modifier = Modifier.weight(3f)
            )
            OutlinedTextField(
                value = realm,
                onValueChange = { onValueChange(it) }
            )
        }
        Button(
            onClick = {
                onClickPress()
                focusManager.clearFocus()
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Buscar")
        }
    }
}