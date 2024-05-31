package com.sergimarrahyarenas.bloodstats.ui.screens.user

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.R
import com.sergimarrahyarenas.bloodstats.data.network.client.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.ui.components.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun OptionsScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope,
) {
    val preferences by userViewModel.userPreferences.observeAsState()
    val user by userViewModel.user.observeAsState()

    val darkTheme by remember { mutableStateOf(preferences?.theme == "dark") }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    BloodStatsTheme(darkTheme = darkTheme) {
        CustomScaffold(
            navController = navController,
            googleAuthUiClient = googleAuthUiClient,
            userViewModel = userViewModel,
            coroutineScope = coroutineScope,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            if (screenWidth < 600.dp) 16.dp else 32.dp
                        ),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(R.string.options_text), style = MaterialTheme.typography.headlineMedium)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(R.string.dark_theme_text))
                        Switch(
                            checked = preferences?.theme == "dark",
                            onCheckedChange = { isChecked ->
                                val newTheme = if (isChecked) "dark" else "light"
                                Log.d("onCheckedChange", newTheme)
                                user?.userUUID?.let {
                                    userViewModel.updateUserTheme(it, newTheme)
                                    userViewModel.getUserWithPreferences(it)
                                }
                            }
                        )
                    }
                    Button(
                        onClick = {
                            navController.navigate(route = Routes.SearchScreen.route)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Arrow Back")
                    }
                }
            }
        )
    }
}