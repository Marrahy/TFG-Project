package com.sergimarrahyarenas.bloodstats.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sergimarrahyarenas.bloodstats.ui.presentation.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.ui.presentation.sign_in.SignInViewModel
import com.sergimarrahyarenas.bloodstats.ui.presentation.sign_in.UserData
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavController,
    userData: UserData?,
    viewModel: SignInViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    context: Context
) {
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
        }
        if (userData?.username != null) {
            Text(
                text = userData.username,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(onClick = {
            viewModel.viewModelScope.launch {
                googleAuthUiClient.signOut()
                Toast.makeText(
                    context,
                    "Signed out",
                    Toast.LENGTH_LONG
                ).show()

                navController.popBackStack()
            }
        }
        ) {
            Text(text = "Sign out")
        }
    }
}