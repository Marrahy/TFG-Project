package com.sergimarrahyarenas.bloodstats.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.ui.common.CustomScaffold
import com.sergimarrahyarenas.bloodstats.models.realm.RealmInfo
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun SearchScreen(
    blizzardViewModel: BlizzardViewModel,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope
) {
    val isLoading: Boolean by blizzardViewModel.isLoading.observeAsState(initial = false)
    val responseError: Boolean by blizzardViewModel.responseError.observeAsState(initial = false)

    var searchedEntity by remember { mutableStateOf("") }
    var realm by remember { mutableStateOf("") }

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
                SearchBox(
                    blizzardViewModel = blizzardViewModel,
                    searchedEntity = searchedEntity,
                    onNameChange = { searchedEntity = it },
                    onRealmChange = { realm = it },
                    onClickPress = {
                        blizzardViewModel.loadCharacterProfileSummaryEquipmentMedia(
                            searchedEntity,
                            realm
                        )
                    }
                )

                Box {
                    if (isLoading) {
                        navController.navigate(route = Routes.LoadingScreen.route)
                    } else if (responseError) {
                        Text(text = "$searchedEntity no coincide con ninguna busqueda")
                    }
                }
            }
        }
    )
}

@Composable
fun SearchBox(
    searchedEntity: String,
    blizzardViewModel: BlizzardViewModel,
    onNameChange: (String) -> Unit,
    onRealmChange: (String) -> Unit,
    onClickPress: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val listOfRealms by blizzardViewModel.listOfRealms.observeAsState(emptyList())
    var selectedRealm by remember { mutableStateOf<RealmInfo?>(null) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                label = { Text(text = "Nombre") },
                value = searchedEntity,
                onValueChange = { onNameChange(it) },
            )
            Box {
                Button(
                    onClick = {
                        expanded = true
                    }
                ) {
                    Text(text = selectedRealm?.name ?: "Reino")
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOfRealms?.forEach { server ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = server.name)
                                },
                                onClick = {
                                    selectedRealm = server
                                    onRealmChange(server.slug)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
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