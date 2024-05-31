package com.sergimarrahyarenas.bloodstats.ui.screens.loginregister

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.R
import com.sergimarrahyarenas.bloodstats.data.network.client.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.ui.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.GoogleViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
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
    BloodStatsTheme {
        if (blizzardViewModel.accessToken.value != null) blizzardViewModel.loadListOfEURealms(
            accessToken = blizzardViewModel.accessToken.value!!
        )

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
                        val userName = signInResult.data.username ?: "defaultName_${UUID.randomUUID()}"
                        val userPassword = signInResult.data.userId
                        val avatar = R.drawable.murlok

                        userViewModel.createIfNotExists(
                            userEmail = email,
                            userName = userName,
                            userPassword = userPassword,
                            avatar = avatar
                        ) { user ->
                            userViewModel.saveUser(user)
                            userViewModel.getUserWithPreferences(user.userUUID)
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
                    context.getString(R.string.welcome_text, userViewModel.user.value?.userName),
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
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            val user by userViewModel.user.observeAsState()

            var userName by rememberSaveable() { mutableStateOf("") }
            var userPassword by rememberSaveable() { mutableStateOf("") }
            var passwordIsVisible by remember { mutableStateOf(false) }
            var selectedAvatar by remember { mutableIntStateOf(R.drawable.murlok) }

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bloodstats_high_resolution_logo),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AvatarSelector(
                selectedAvatar = selectedAvatar,
                onAvatarSelected = { avatar ->
                    selectedAvatar = avatar
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text(text = stringResource(R.string.user_name_label)) },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = userPassword,
                onValueChange = { userPassword = it },
                label = { Text(text = stringResource(R.string.password_label)) },
                singleLine = true,
                visualTransformation = if (passwordIsVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image =
                        if (passwordIsVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(
                        onClick = {
                            passwordIsVisible = !passwordIsVisible
                        }
                    ) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (passwordIsVisible) "Hide password" else "Show password"
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            verifyUser(
                                userViewModel,
                                userName,
                                userPassword,
                                navController,
                                context
                            )
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary)
                ) {
                    Text(
                        text = stringResource(R.string.login_text_button),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (userName.isBlank() || userPassword.isBlank()) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.user_password_verification_error_text),
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            coroutineScope.launch {
                                if (userViewModel.checkIfUserExists(userName)) {
                                    userViewModel.createIfNotExists(
                                        userEmail = null,
                                        userName = userName,
                                        userPassword = userPassword,
                                        avatar = selectedAvatar,
                                    ) {
                                        coroutineScope.launch {
                                            verifyUser(
                                                userViewModel,
                                                userName,
                                                userPassword,
                                                navController,
                                                context
                                            )
                                        }
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.user_name_verification_error_text),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary)
                ) {
                    Text(text = stringResource(R.string.register_text_button), color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
            ) {
                Text(
                    text = stringResource(R.string.sign_in_google_text_button),
                    color = MaterialTheme.colorScheme.onSecondary
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Image(
                    painter = painterResource(id = R.drawable.google_logo_google_icongoogle_512),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

suspend fun verifyUser(
    userViewModel: UserViewModel,
    userName: String,
    userPassword: String,
    navController: NavController,
    context: Context
) {
    val isValid = userViewModel.verifyUserCredentials(userName, userPassword)
    withContext(Dispatchers.Main) {
        if (isValid) {
            userViewModel.user.value?.userUUID?.let { userUUID ->
                userViewModel.getUserWithPreferences(
                    userUUID
                )
            }
            navController.navigate(route = Routes.SearchScreen.route)
            Toast.makeText(
                context,
                context.getString(R.string.welcome_text, userViewModel.user.value?.userName),
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.user_name_password_error_text),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

@Composable
fun AvatarSelector(
    selectedAvatar: Int,
    onAvatarSelected: (Int) -> Unit
) {
    val avatarList = listOf(
        R.drawable.murlok,
        R.drawable.frog,
        R.drawable.hunter,
        R.drawable.mage,
        R.drawable.rogue,
        R.drawable.shaman,
        R.drawable.spacemurlok,
        R.drawable.sylvanas,
        R.drawable.warrior
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        items(avatarList) { avatar ->
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .clickable { onAvatarSelected(avatar) }
                    .then(
                        if (avatar == selectedAvatar) {
                            Modifier.border(4.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
                        } else {
                            Modifier
                        }
                    )
            ) {
                Image(
                    painter = painterResource(id = avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .padding(4.dp)
                )
            }
        }
    }
}
