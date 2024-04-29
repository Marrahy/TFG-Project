package com.sergimarrahyarenas.bloodstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.sergimarrahyarenas.api.viewmodel.BlizzardViewModel
import com.sergimarrahyarenas.bloodstats.navigation.Navigation
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.core.presentation.sign_in.SignInViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent {
                val context = LocalContext.current
                val blizzardViewModel by viewModels<BlizzardViewModel>()
                val googleViewModel by viewModels<SignInViewModel>()

                Navigation(context = context, blizzardViewModel, googleViewModel)
            }
        }
    }
}

@Composable
fun AppContent(content: @Composable () -> Unit) {
    BloodStatsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}