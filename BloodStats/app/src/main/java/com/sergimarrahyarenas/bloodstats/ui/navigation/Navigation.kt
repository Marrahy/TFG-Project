package com.sergimarrahyarenas.bloodstats.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.sergimarrahyarenas.bloodstats.data.network.client.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.ui.screens.characterdata.CharacterDungeonsScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.characterdata.CharacterEquipmentScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.characterdata.CharacterGuildScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.characterdata.CharacterSpecializationScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.characterdata.CharacterStatisticsScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.itemdata.ItemDataScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.intro.LoadingScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.loginregister.LoginScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.user.OptionsScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.user.ProfileScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.search.SearchScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.intro.SplashScreen
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.GoogleViewModel
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Composable
fun Navigation(userViewModel: UserViewModel, context: Context) {
    val navController = rememberNavController()

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    val blizzardViewModel = BlizzardViewModel()
    val googleViewModel = GoogleViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route
    ) {
        composable(
            route = Routes.SplashScreen.route
        ) {
            SplashScreen(
                navController = navController,
                blizzardViewModel = blizzardViewModel
            )
        }
        composable(
            route = Routes.LoginScreen.route
        ) {
            LoginScreen(
                navController = navController,
                googleViewModel = googleViewModel,
                blizzardViewModel = blizzardViewModel,
                userViewModel = userViewModel,
                googleAuthUiClient = googleAuthUiClient,
                context = context,
                coroutineScope = coroutineScope
            )
        }
        composable(
            route = Routes.OptionsScreen.route
        ) {
            OptionsScreen(
                navController = navController,
                userViewModel = userViewModel,
                googleAuthUiClient = googleAuthUiClient,
                coroutineScope = coroutineScope,
            )
        }
        composable(
            route = Routes.SearchScreen.route
        ) {
            SearchScreen(
                blizzardViewModel = blizzardViewModel,
                navController = navController,
                userViewModel = userViewModel,
                googleAuthUiClient = googleAuthUiClient,
                coroutineScope = coroutineScope,
                context = context
            )
        }
        composable(
            route = Routes.LoadingScreen.route
        ) {
            LoadingScreen(
                navController = navController,
                blizzardViewModel = blizzardViewModel,
                context = context,
                userViewModel = userViewModel,
                userData = googleAuthUiClient.getSignInUser()
            )
        }
        composable(
            route = Routes.ProfileScreen.route
        ) {
            ProfileScreen(
                navController = navController,
                userData = googleAuthUiClient.getSignInUser(),
                googleAuthUiClient = googleAuthUiClient,
                context = context,
                googleV = googleViewModel,
                userViewModel = userViewModel,
                coroutineScope = coroutineScope
            )
        }
        composable(
            route = Routes.CharacterStatisticsScreen.route
        ) {
            CharacterStatisticsScreen(
                blizzardViewModel = blizzardViewModel,
                userViewModel = userViewModel,
                navController = navController,
                googleAuthUiClient = googleAuthUiClient,
                coroutineScope = coroutineScope,
                context = context
            )
        }
        composable(
            route = Routes.CharacterEquipmentScreen.route
        ) {
            CharacterEquipmentScreen(
                blizzardViewModel = blizzardViewModel,
                userViewModel = userViewModel,
                navController = navController,
                googleAuthUiClient = googleAuthUiClient,
                coroutineScope = coroutineScope,
            )
        }
        composable(
            route = Routes.CharacterSpecializationScreen.route
        ) {
            CharacterSpecializationScreen(
                navController = navController,
                blizzardViewModel = blizzardViewModel,
                userViewModel = userViewModel,
                googleAuthUiClient = googleAuthUiClient,
                coroutineScope = coroutineScope,
            )
        }
        composable(
            route = Routes.CharacterDungeonsScreen.route
        ) {
            CharacterDungeonsScreen(
                navController = navController,
                blizzardViewModel = blizzardViewModel,
                userViewModel = userViewModel,
                googleAuthUiClient = googleAuthUiClient,
                coroutineScope = coroutineScope,
                )
        }
        composable(
            route = Routes.CharacterGuildScreen.route
        ) {
            CharacterGuildScreen(
                blizzardViewModel = blizzardViewModel,
                userViewModel = userViewModel,
                googleAuthUiClient = googleAuthUiClient,
                navController = navController,
                coroutineScope = coroutineScope,
            )
        }
        composable(
            route = Routes.ItemDataScreen.route
        ) {
            ItemDataScreen(
                navController = navController,
                blizzardViewModel = blizzardViewModel,
                userViewModel = userViewModel,
                googleAuthUiClient = googleAuthUiClient,
                coroutineScope = coroutineScope
            )
        }
    }
}
