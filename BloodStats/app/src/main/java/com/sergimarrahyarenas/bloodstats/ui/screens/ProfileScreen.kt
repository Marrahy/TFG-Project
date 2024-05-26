package com.sergimarrahyarenas.bloodstats.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.UserData
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.common.CustomScaffold
import com.sergimarrahyarenas.bloodstats.viewmodel.GoogleViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: GoogleViewModel,
    userViewModel: UserViewModel,
    userData: UserData?,
    googleAuthUiClient: GoogleAuthUiClient,
    context: Context,
    coroutineScope: CoroutineScope
) {
    var showDialog by remember { mutableStateOf(false) }
    val userAvatar by userViewModel.user.observeAsState()
    val userWithFavorites by userViewModel.userWithFavorites.observeAsState()

    LaunchedEffect(key1 = userData?.userId) {
        userData?.userId?.let { userId ->
            userViewModel.getUserWithPreferences(userId)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Eliminar cuenta") },
            text = { Text(text = "¿Estás seguro de que deseas eliminar la cuenta permanentemente?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        userViewModel.user.let {
                            coroutineScope.launch {
                                userViewModel.user.value?.let {
                                    userViewModel.deleteUser(
                                        it.userUUID,
                                        context = context
                                    )
                                }
                                googleAuthUiClient.signOut()
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "Cuenta eliminada",
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
                    Text(text = "Aceptar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "Cancelar")
                }
            }
        )
    }

    CustomScaffold(
        navController = navController,
        googleAuthUiClient = googleAuthUiClient,
        coroutineScope = coroutineScope,
        context = context,
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
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
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Favoritos")
                userWithFavorites?.favorites?.let { favorites ->
                    LazyColumn {
                        items(favorites) { favorite ->
                            Text(
                                text = "${favorite.characterName} - ${favorite.characterRealmSlug} - ${favorite.characterMythicRating}",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
                Button(
                    onClick = {
                        viewModel.viewModelScope.launch {
                            googleAuthUiClient.signOut()
                            Toast.makeText(
                                context,
                                "Sesión cerrada",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.navigate(route = Routes.LoginScreen.route) {
                                popUpTo(route = Routes.LoginScreen.route) { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "Cerrar sesión")
                }

                Button(
                    onClick = {
                        showDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onErrorContainer)
                ) {
                    Text(text = "Eliminar cuenta")
                }
            }
        }
    )
}