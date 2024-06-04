package com.sergimarrahyarenas.bloodstats.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.R
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.data.network.client.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.CharacterProfileSummary
import com.sergimarrahyarenas.bloodstats.ui.navigation.Routes
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(
    content: @Composable () -> Unit,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    userViewModel: UserViewModel,
    coroutineScope: CoroutineScope,
) {
    val user by userViewModel.user.observeAsState()
    val preferences by userViewModel.userPreferences.observeAsState()

    val darkTheme = preferences?.theme == "dark"


    BloodStatsTheme(darkTheme = darkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.title_scaffold_text)) },
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
                                text = { Text(text = stringResource(R.string.menu_profile_text)) },
                                onClick = {
                                    navController.navigate(route = Routes.ProfileScreen.route)
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Profile"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.menu_search_text)) },
                                onClick = {
                                    navController.navigate(route = Routes.SearchScreen.route)
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.menu_options_text)) },
                                onClick = {
                                    navController.navigate(route = Routes.OptionsScreen.route)
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Options"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.signout_text)) },
                                onClick = {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        googleAuthUiClient.signOut()
                                        userViewModel.clearUser()
                                    }
                                    navController.navigate(route = Routes.LoginScreen.route)
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "Signout"
                                    )
                                }
                            )
                        }
                    },
                    actions = {
                        user?.avatarId.let { avatarResource ->
                            IconButton(
                                onClick = {
                                    navController.navigate(route = Routes.ProfileScreen.route)
                                }
                            ) {
                                avatarResource?.let { painterResource(id = it) }?.let {
                                    Image(
                                        painter = it,
                                        contentDescription = "Avatar",
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                    )
                                }
                            }
                        }
                    }
                )
            },
            content = {
                BoxWithConstraints(
                    modifier = Modifier.padding(it)
                ) {
                    val constraint = this@BoxWithConstraints
                    val maxWidth = constraint.maxWidth
                    val padding = if (maxWidth < 600.dp) 8.dp else 16.dp

                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.padding(padding)
                    ) {
                        content()
                    }
                }
            }
        )
    }
}

@Composable
fun DynamicButton(
    currentScreen: String,
    navController: NavController,
    blizzardViewModel: BlizzardViewModel,
    characterProfileSummary: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.CharacterProfileSummary?,
    characterActiveSpecialization: String?,
    userViewModel: UserViewModel
) {
    val preferences by userViewModel.userPreferences.observeAsState()
    val darkTheme = preferences?.theme == "dark"

    val buttonList = allScreens.filter { it != currentScreen }

    BloodStatsTheme(darkTheme = darkTheme) {
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
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    val screenName = when (screen) {
                        Routes.CharacterEquipmentScreen.route -> stringResource(R.string.equipment_text)
                        Routes.CharacterGuildScreen.route -> stringResource(R.string.guild_text)
                        Routes.CharacterSpecializationScreen.route -> stringResource(id = R.string.specialization_text)
                        Routes.CharacterStatisticsScreen.route -> stringResource(R.string.attributes_text)
                        Routes.CharacterDungeonsScreen.route -> stringResource(R.string.dungeons_text)
                        else -> ""
                    }
                    Text(text = screenName)
                }
            }
        }
    }
}

@Composable
fun TitleScreen(title: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    Spacer(modifier = Modifier.padding(8.dp))
}