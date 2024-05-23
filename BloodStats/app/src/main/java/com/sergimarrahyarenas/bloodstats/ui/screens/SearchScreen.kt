package com.sergimarrahyarenas.bloodstats.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.models.realm.RealmInfo
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.common.CustomScaffold
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SearchScreen(
    blizzardViewModel: BlizzardViewModel,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope,
    context: Context
) {
    val responseError by blizzardViewModel.responseError.observeAsState()
    var showError by remember { mutableStateOf(false) }
    var searchedEntity by remember { mutableStateOf("") }
    var realm by remember { mutableStateOf("") }


    LaunchedEffect(responseError) {
        if (responseError == true) {
            showError = true
        } else if (!responseError!! && searchedEntity.isNotEmpty() && realm != "Reino") {
            showError = false
        }
    }

    LaunchedEffect(showError) {
        if (showError) {
            Toast.makeText(
                context,
                "Nombre del personaje o reino incorrectos",
                Toast.LENGTH_LONG
            ).show()
            showError = false
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
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                SearchBox(
                    blizzardViewModel = blizzardViewModel,
                    searchedEntity = searchedEntity,
                    onNameChange = { searchedEntity = it },
                    onRealmChange = { realm = it },
                    onClickPress = {
                        if (!showError) {
                            navController.navigate(route = Routes.CharacterEquipmentScreen.route)
                            blizzardViewModel.loadCharacterProfileSummaryEquipmentMedia(
                                searchedEntity,
                                realm
                            )
                        }
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                label = { Text(text = "Nombre del personaje") },
                value = searchedEntity,
                onValueChange = { onNameChange(it) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Box {
                Button(
                    onClick = {
                        expanded = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = selectedRealm?.name ?: "Reino")
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
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
                                },
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
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = "Buscar")
        }
    }
} 