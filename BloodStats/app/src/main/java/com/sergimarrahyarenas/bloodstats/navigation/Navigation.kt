package com.sergimarrahyarenas.bloodstats.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.sergimarrahyarenas.api.ViewModel
import com.sergimarrahyarenas.core.presentation.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.core.presentation.sign_in.SignInViewModel
import com.sergimarrahyarenas.bloodstats.ui.screens.LoginScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.MainScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.ProfileScreen
import com.sergimarrahyarenas.bloodstats.ui.screens.SplashScreen

@Composable
fun Navigation(
    viewModel: SignInViewModel,
    context: Context,
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
                viewModel = viewModel,
                googleAuthUiClient = googleAuthUiClient
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
            MainScreen()
        }
        composable(
            route = Routes.ProfileScreen.route
        ) {
            ProfileScreen(
                navController = navController,
                userData = googleAuthUiClient.getSignInUser(),
                viewModel = viewModel,
                googleAuthUiClient = googleAuthUiClient,
                context = context
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