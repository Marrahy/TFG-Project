package com.sergimarrahyarenas.bloodstats.ui.screens.user

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sergimarrahyarenas.bloodstats.R
import com.sergimarrahyarenas.bloodstats.data.network.client.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.model.google.signinresult.UserData
import com.sergimarrahyarenas.bloodstats.ui.components.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.viewmodel.GoogleViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen(
    navController: NavController,
    googleV: GoogleViewModel,
    userViewModel: UserViewModel,
    userData: UserData?,
    googleAuthUiClient: GoogleAuthUiClient,
    context: Context,
    coroutineScope: CoroutineScope
) {
    var showDialog by remember { mutableStateOf(false) }
    val userAvatar by userViewModel.user.observeAsState()
    val user by userViewModel.user.observeAsState()
    val userFavorites by userViewModel.userFavorites.observeAsState()
    val preferences by userViewModel.userPreferences.observeAsState()

    val darkTheme = preferences?.theme == "dark"


    LaunchedEffect(key1 = true) {
        user?.userUUID?.let { userViewModel.getFavorites(it) }
    }

    BloodStatsTheme(darkTheme = darkTheme) {
        CustomScaffold(
            navController = navController,
            googleAuthUiClient = googleAuthUiClient,
            userViewModel = userViewModel,
            coroutineScope = coroutineScope,
            content = {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (userData?.profilePictureUrl != null) {
                        AsyncImage(
                            model = userData.profilePictureUrl,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        userAvatar?.avatarId?.let { painterResource(id = it) }?.let {
                            Image(
                                painter = it,
                                contentDescription = "User Avatar",
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    if (userData?.username != null) {
                        Text(
                            text = userData.username,
                            textAlign = TextAlign.Center,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        user?.userName?.let {
                            Text(
                                text = it,
                                textAlign = TextAlign.Center,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(id = R.string.favorites_text),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (userFavorites != null) {
                        userFavorites?.let { favorites ->
                            LazyColumn {
                                items(favorites) { favorite ->
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "${favorite.characterName} - ${favorite.characterRealmSlug} - ${favorite.characterMythicRating}",
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(8.dp)
                                        )

                                        Spacer(modifier = Modifier.padding(8.dp))

                                        Icon(
                                            imageVector = Icons.Outlined.Stars,
                                            contentDescription = "Rating",
                                            tint = Color.Yellow
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                googleV.viewModelScope.launch {
                                    googleAuthUiClient.signOut()
                                    userViewModel.clearUser()
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.signout_text),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    navController.navigate(route = Routes.LoginScreen.route) {
                                        popUpTo(route = Routes.LoginScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(text = stringResource(R.string.signout_text_button))
                        }

                        Button(
                            onClick = {
                                showDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onErrorContainer)
                        ) {
                            Text(text = stringResource(R.string.delete_account_text_button))
                        }
                    }
                    Button(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Arrow Back")
                    }
                }
            }
        )

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = stringResource(R.string.delete_account_text)) },
                text = { Text(text = stringResource(R.string.warning_message_text)) },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            userViewModel.user.let {
                                coroutineScope.launch {
                                    userViewModel.user.value?.let {
                                        userViewModel.deleteUser(it.userUUID)
                                    }
                                    googleAuthUiClient.signOut()
                                    userViewModel.clearUser()
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.account_delete_text),
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.navigate(route = Routes.LoginScreen.route) {
                                            popUpTo(Routes.ProfileScreen.route) { inclusive = true }
                                        }
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onErrorContainer)
                    ) {
                        Text(text = stringResource(R.string.accept_text_button))
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(text = stringResource(R.string.cancel_text_button))
                    }
                }
            )
        }
    }
}