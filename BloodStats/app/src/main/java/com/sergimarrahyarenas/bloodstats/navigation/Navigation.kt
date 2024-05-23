package com.sergimarrahyarenas.bloodstats.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.ui.screens.CharacterDungeonsScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.CharacterEquipmentScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.CharacterGuildScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.CharacterSpecializationScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.CharacterStatisticsScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.ItemDataScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.LoadingScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.LoginScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.NpcDataScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.OptionsScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.ProfileScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.RegisterScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.SearchScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.SplashScreen
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
        //SplashScreen
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
            route = Routes.RegisterScreen.route
        ) {
            RegisterScreen(navController = navController)
        }
        composable(
            route = Routes.OptionsScreen.route
        ) {
            OptionsScreen(navController = navController)
        }
        composable(
            route = Routes.SearchScreen.route
        ) {
            SearchScreen(
                blizzardViewModel = blizzardViewModel,
                navController = navController,
                googleAuthUiClient = googleAuthUiClient,
                coroutineScope = coroutineScope,
                context = context
            )
        }
        composable(
            route = Routes.LoadingScreen.route
        ) {
            LoadingScreen(navController = navController)
        }
        composable(
            route = Routes.ProfileScreen.route
        ) {
            ProfileScreen(
                navController = navController,
                userData = googleAuthUiClient.getSignInUser(),
                googleAuthUiClient = googleAuthUiClient,
                context = context,
                viewModel = googleViewModel,
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
                navController = navController,
                googleAuthUiClient = googleAuthUiClient,
                coroutineScope = coroutineScope,
                context = context
            )
        }
        composable(
            route = Routes.CharacterSpecializationScreen.route
        ) {
            CharacterSpecializationScreen(
                navController = navController,
                blizzardViewModel = blizzardViewModel,
                googleAuthUiClient = googleAuthUiClient,
                coroutineScope = coroutineScope,
                context = context
            )
        }
        composable(
            route = Routes.CharacterDungeonsScreen.route
        ) {
            CharacterDungeonsScreen(
                navController = navController,
                blizzardViewModel = blizzardViewModel
            )
        }
        composable(
            route = Routes.CharacterGuildScreen.route
        ) {
            CharacterGuildScreen(
                blizzardViewModel = blizzardViewModel,
                googleAuthUiClient = googleAuthUiClient,
                navController = navController,
                coroutineScope = coroutineScope,
                context = context
            )
        }
        composable(
            route = Routes.ItemDataScreen.route
        ) {
            ItemDataScreen(
                navController = navController,
                blizzardViewModel = blizzardViewModel,
                googleAuthUiClient = googleAuthUiClient,
                coroutineScope = coroutineScope,
                context = context
            )
        }
        composable(
            route = Routes.NpcDataScreen.route
        ) {
            NpcDataScreen(navController = navController)
        }
    }
}
