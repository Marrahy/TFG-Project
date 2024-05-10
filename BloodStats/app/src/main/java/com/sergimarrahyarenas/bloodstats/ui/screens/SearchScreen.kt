package com.sergimarrahyarenas.bloodstats.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.sergimarrahyarenas.bloodstats.navigation.Routes

@Composable
fun SearchScreen(
    blizzardViewModel: BlizzardViewModel,
    navController: NavController
) {
    val isLoading: Boolean by blizzardViewModel.isLoading.observeAsState(initial = false)
    val responseError: Boolean by blizzardViewModel.responseError.observeAsState(initial = false)

    var searchedEntity by remember { mutableStateOf("") }
    var realm by remember { mutableStateOf("") }

    SearchBox(
        searchedEntity = searchedEntity,
        realm = realm,
        onNameChange = { searchedEntity = it },
        onRealmChange = {
            if (it != null) {
                realm = it
            }
        },
        onClickPress = {
            blizzardViewModel.loadCharacterProfileSummaryEquipmentMedia(searchedEntity, realm)
        }
    )

    Box {
        if (isLoading) {
            navController.navigate(route = Routes.LoadingScreen.route)
        } else if (responseError) {
            Text(text = "\"$searchedEntity\" no coincide con ninguna busqueda")
        }
    }
}

@Composable
fun SearchBox(
    modifier: Modifier = Modifier,
    searchedEntity: String,
    realm: String,
    onNameChange: (String) -> Unit,
    onRealmChange: (String?) -> Unit,
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
                label = { Text(text = "Nombre") },
                value = searchedEntity,
                onValueChange = { onNameChange(it) },
            )
            OutlinedTextField(
                label = { Text(text = "Servidor") },
                value = realm,
                onValueChange = { onRealmChange(it) }
            )
        }
        Button(
            onClick = {
                onClickPress()
                focusManager.clearFocus()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Buscar")
        }
    }
}