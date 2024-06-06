package com.sergimarrahyarenas.bloodstats.ui.screens.intro

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sergimarrahyarenas.bloodstats.R
import com.sergimarrahyarenas.bloodstats.ui.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun LoadingScreen(
    navController: NavController,
    blizzardViewModel: BlizzardViewModel,
    userViewModel: UserViewModel,
    context: Context
) {
    val checkIfNull by blizzardViewModel.characterProfileSummary.observeAsState()
    val preferences by userViewModel.userPreferences.observeAsState()
    val user by userViewModel.user.observeAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ),
        label = "Float Animation"
    )

    val darkTheme = preferences?.theme == "dark"

    BloodStatsTheme(darkTheme = darkTheme) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                user?.avatarId?.let { painterResource(id = it) }?.let {
                    Image(
                        painter = it,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(250.dp)
                            .clip(CircleShape)
                            .rotate(angle),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Text(
                text = stringResource(id = R.string.loading_text),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    LaunchedEffect(checkIfNull) {
        delay(5500)
        if (checkIfNull?.name != null) {
            navController.navigate(route = Routes.CharacterEquipmentScreen.route)
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    context.getString(R.string.character_search_error_text),
                    Toast.LENGTH_LONG
                ).show()
            }
            navController.popBackStack()
        }
    }
}