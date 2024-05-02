package com.sergimarrahyarenas.bloodstats.ui.screens

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.common.CustomScaffold
import com.sergimarrahyarenas.bloodstats.ui.presentation.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.ui.presentation.sign_in.GoogleViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    context: Context,
    viewModel: GoogleViewModel,
) {
    val customScaffold = CustomScaffold()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.viewModelScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResult(signInResult)
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

            navController.navigate("profile_screen")
            viewModel.resetState()
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
            navController.navigate("profile_screen")
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        var userName by rememberSaveable() {
            mutableStateOf("")
        }
        var userPassword by rememberSaveable() {
            mutableStateOf("")
        }

        //Image(painter = painterResource(id = R.drawable.dogo), contentDescription = "GoodBoy")

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text(text = "User Name") },
            singleLine = true
        )

        OutlinedTextField(
            value = userPassword,
            onValueChange = { userPassword = it },
            label = { Text(text = "Password") },
            singleLine = true
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    viewModel.viewModelScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                },
            ) {
                Text(text = "Sign in")
            }

            Button(
                onClick = {
                    navController.navigate("main_screen")
                },
            ) {
                Text(text = "Get Token")
            }
        }
    }
}
