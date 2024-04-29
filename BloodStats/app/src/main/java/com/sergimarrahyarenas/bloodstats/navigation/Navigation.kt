package com.sergimarrahyarenas.bloodstats.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.sergimarrahyarenas.api.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.ui.presentation.sign_in.SignInViewModel
import com.sergimarrahyarenas.bloodstats.ui.screens.LoginScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.MainScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.ProfileScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.SplashScreen
import com.sergimarrahyarenas.core.presentation.sign_in.GoogleAuthUiClient

@Composable
fun Navigation(
    context: Context,
    blizzardViewModel: BlizzardViewModel,
    signInViewModel: SignInViewModel
) {
    val navController = rememberNavController()
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route
    ) {
        //SplashScreen
        composable(
            route = Routes.SplashScreen.route
        ) {
            SplashScreen(navController = navController)
        }
        composable(
            route = Routes.LoginScreen.route
        ) {
            LoginScreen(
                navController = navController,
                googleAuthUiClient = googleAuthUiClient,
                context = context,
                viewModel = signInViewModel
            )
        }
//        composable(
//            route = Routes.RegisterScreen.route
//        ) {
//            RegisterScreen(navController = navController)
//        }
        composable(
            route = Routes.MainScreen.route
        ) {
            MainScreen(
                blizzardViewModel = blizzardViewModel
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
                viewModel = signInViewModel
            )
        }
//        composable(
//            route = Routes.CharacterGeneralInfoScreen.route
//        ) {
//            CharacterGeneralInfoScreen(navController = navController)
//        }
//        composable(
//            route = Routes.CharacterStatsScreen.route
//        ) {
//            CharacterStatsScreen(navController = navController)
//        }
//        composable(
//            route = Routes.CharacterSkillTreeScreen.route
//        ) {
//            CharacterSkillTreeScreen(navController = navController)
//        }
//        composable(
//            route = Routes.CharacterDungeonsScreen.route
//        ) {
//            CharacterDungeonsScreen(navController = navController)
//        }
//        composable(
//            route = Routes.CharacterGuildScreen.route
//        ) {
//            CharacterGuildScreen(navController = navController)
//        }
//        composable(
//            route = Routes.BuildPlannerScreen.route
//        ) {
//            BuildPlannerScreen(navController = navController)
//        }
//        composable(
//            route = Routes.BossGeneralInfoScreen.route
//        ) {
//            BossGeneralInfoScreen(navController = navController)
//        }
//        composable(
//            route = Routes.CreatureGeneralInfoScreen.route
//        ) {
//            CreatureGeneralInfoScreen(navController = navController)
//        }
//        composable(
//            route = Routes.NpcGeneralInfoScreen.route
//        ) {
//            NpcGeneralInfoScreen(navController = navController)
//        }
    }
}
