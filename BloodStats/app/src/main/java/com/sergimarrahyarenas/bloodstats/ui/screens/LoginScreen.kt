package com.sergimarrahyarenas.bloodstats.ui.screens

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.database.entities.UserEntity
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.common.CustomScaffold
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.GoogleViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

@Composable
fun LoginScreen(
    navController: NavController,
    googleViewModel: GoogleViewModel,
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    context: Context,
    coroutineScope: CoroutineScope
) {
    if (blizzardViewModel.accessToken.value != null) blizzardViewModel.loadListOfEURealms(
        accessToken = blizzardViewModel.accessToken.value!!
    )

    CustomScaffold(
        navController = navController,
        googleAuthUiClient = googleAuthUiClient,
        coroutineScope = coroutineScope,
        content = {
            val state by googleViewModel.state.collectAsStateWithLifecycle()
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        coroutineScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            googleViewModel.onSignInResult(signInResult)
                            val email = signInResult.data?.userEmail ?: return@launch
                            val userName =
                                signInResult.data.username ?: "defaultName_${UUID.randomUUID()}"
                            val userPassword = signInResult.data.userId

                            userViewModel.createIfNotExists(
                                userEmail = email,
                                userName = userName,
                                userPassword = userPassword
                            ) {
                                navController.navigate(route = Routes.SearchScreen.route)
                            }
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        context,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate(route = Routes.SearchScreen.route)
                    googleViewModel.resetState()
                }
            }

            LaunchedEffect(key1 = state.signInError) {
                state.signInError?.let { error ->
                    Toast.makeText(
                        context,
                        error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignInUser() != null) {
                    navController.navigate(route = Routes.SearchScreen.route)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                var userName by rememberSaveable() { mutableStateOf("") }
                var userPassword by rememberSaveable() { mutableStateOf("") }

                //Image(painter = painterResource(id = R.drawable.dogo), contentDescription = "GoodBoy")

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text(text = "Nombre de usuario") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = userPassword,
                    onValueChange = { userPassword = it },
                    label = { Text(text = "Contraseña") },
                    singleLine = true
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            googleViewModel.viewModelScope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        },
                    ) {
                        Text(text = "Iniciar Sesión con Google")
                    }

                    Button(
                        onClick = {
                            userViewModel.createIfNotExists(
                                userEmail = null,
                                userName = userName,
                                userPassword = userPassword
                            ) {
                                navController.navigate(route = Routes.SearchScreen.route)
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = "Registrarse")
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val isValid = userViewModel.verifyUserCredentials(userName, userPassword)
                                withContext(Dispatchers.Main) {
                                    if (isValid) {
                                        navController.navigate(route = Routes.SearchScreen.route)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Credenciales incorrectas",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = "Iniciar Sesión")
                    }
                }
            }
        }
    )
}
