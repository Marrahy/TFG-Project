package com.sergimarrahyarenas.bloodstats.ui.screens.search

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.R
import com.sergimarrahyarenas.bloodstats.data.network.client.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.realm.RealmInfo
import com.sergimarrahyarenas.bloodstats.ui.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.components.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun SearchScreen(
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope,
    context: Context
) {
    val preferences by userViewModel.userPreferences.observeAsState()

    var showError by remember { mutableStateOf(false) }
    var searchedEntity by remember { mutableStateOf("") }
    var realm by remember { mutableStateOf("") }

    val darkTheme = preferences?.theme == "dark"

    LaunchedEffect(showError) {
        if (showError) {
            Toast.makeText(
                context,
                context.getString(R.string.character_name_realm_error_text),
                Toast.LENGTH_LONG
            ).show()
            showError = false
        }
    }

    BloodStatsTheme(darkTheme = darkTheme) {
        CustomScaffold(
            navController = navController,
            googleAuthUiClient = googleAuthUiClient,
            userViewModel = userViewModel,
            coroutineScope = coroutineScope,
            content = {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val maxWidth = this@BoxWithConstraints.maxWidth
                    val padding = if (maxWidth < 600.dp) 8.dp else 16.dp

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        SearchBox(
                            blizzardViewModel = blizzardViewModel,
                            searchedEntity = searchedEntity,
                            onNameChange = { searchedEntity = it },
                            onRealmChange = { realm = it },
                            onClickPress = {
                                if (searchedEntity.isNotBlank() && realm.isNotBlank()) {
                                    navController.navigate(route = Routes.LoadingScreen.route)
                                    blizzardViewModel.clearCharacterData()
                                    blizzardViewModel.loadCharacterProfileSummaryEquipmentMedia(
                                        searchedEntity,
                                        realm
                                    )
                                } else {
                                    showError = !showError
                                }
                            },
                            userViewModel = userViewModel
                        )
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(
    searchedEntity: String,
    blizzardViewModel: BlizzardViewModel,
    onNameChange: (String) -> Unit,
    onRealmChange: (String) -> Unit,
    onClickPress: () -> Unit,
    userViewModel: UserViewModel
) {
    val focusManager = LocalFocusManager.current
    val listOfRealms by blizzardViewModel.listOfRealms.observeAsState(emptyList())
    val preferences by userViewModel.userPreferences.observeAsState()

    var selectedRealm by remember { mutableStateOf<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.realm.RealmInfo?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val darkTheme = preferences?.theme == "dark"

    val groupedRealms = listOfRealms?.groupBy { it.name.first() }

    BloodStatsTheme(darkTheme = darkTheme) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.character_name_text)) },
                    value = searchedEntity,
                    onValueChange = { onNameChange(it) },
                    modifier = Modifier.weight(0.7f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                )

                Spacer(modifier = Modifier.padding(4.dp))

                Box(
                    modifier = Modifier.weight(0.3f)
                ) {
                    Button(
                        onClick = {
                            expanded = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(text = selectedRealm?.name ?: stringResource(R.string.realm_text))

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth(0.7f)
                        ) {
                            groupedRealms?.forEach { (initial, realms) ->
                                DropdownMenuItem(
                                    text = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    MaterialTheme.colorScheme.primary.copy(
                                                        alpha = 0.2f
                                                    )
                                                )
                                        ) {
                                            Text(
                                                text = initial.toString(),
                                                style = MaterialTheme.typography.labelLarge,
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    },
                                    onClick = {}
                                )

                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = Color.Black,
                                    thickness = 2.dp
                                )

                                realms.forEach { server ->
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
            }
            Button(
                onClick = {
                    onClickPress()
                    focusManager.clearFocus()
                },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = stringResource(R.string.search_text_button))
            }
        }
    }
}