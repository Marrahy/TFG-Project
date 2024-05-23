package com.sergimarrahyarenas.bloodstats.ui.common

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.models.characterprofilesummary.CharacterProfileSummary
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import com.sergimarrahyarenas.bloodstats.preferences.SharedPreferencesUtils
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(
    content: @Composable () -> Unit,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope,
    context: Context
    ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "BloodStats") },
                navigationIcon = {
                    var expanded by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Perfil") },
                            onClick = {
                                navController.navigate(route = Routes.ProfileScreen.route)
                            },
                            trailingIcon = {
                                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Perfil")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Buscar") },
                            onClick = {
                                navController.navigate(route = Routes.SearchScreen.route)
                            },
                            trailingIcon = {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Opciones") },
                            onClick = {
                                navController.navigate(route = Routes.OptionsScreen.route)
                            },
                            trailingIcon = {
                                Icon(imageVector = Icons.Default.Settings, contentDescription = "Opciones")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Cerrar sesión") },
                            onClick = {
                                coroutineScope.launch(Dispatchers.IO) {
                                    googleAuthUiClient.signOut()
                                    SharedPreferencesUtils.clearUserUUID(context)
                                }
                                navController.navigate(route = Routes.LoginScreen.route)
                            },
                            trailingIcon = {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                            }
                        )
                    }
                }
            )
        },
        content = {
            Surface(
                color = Color.White,
                modifier = Modifier.padding(it)
            ) {
                content()
            }
        }
    )
}

@Composable
fun DynamicButton(
    currentScreen: String,
    navController: NavController,
    blizzardViewModel: BlizzardViewModel,
    characterProfileSummary: CharacterProfileSummary?,
    characterActiveSpecialization: String?,
) {
    val buttonList = allScreens.filter { it != currentScreen }

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(buttonList) { screen ->
            Button(
                onClick = {
                    ButtonNavigationManagement.handleNavigation(
                        screen = screen,
                        navController = navController,
                        blizzardViewModel = blizzardViewModel,
                        characterProfileSummary = characterProfileSummary,
                        characterActiveSpecialization = characterActiveSpecialization,
                    )
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                val screenName = when (screen) {
                    Routes.CharacterEquipmentScreen.route -> "Equipamiento"
                    Routes.CharacterGuildScreen.route -> "Clan"
                    Routes.CharacterSpecializationScreen.route -> "Especialización"
                    Routes.CharacterStatisticsScreen.route -> "Atributos"
                    Routes.CharacterDungeonsScreen.route -> "Mazmorras"
                    else -> ""
                }
                Text(text = screenName)
            }
        }
    }
}