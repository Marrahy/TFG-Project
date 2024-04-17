package com.sergimarrahyarenas.bloodstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sergimarrahyarenas.bloodstats.navigation.Navigation
import com.sergimarrahyarenas.bloodstats.ui.presentation.sign_in.SignInViewModel
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent {
                val viewModel = viewModel<SignInViewModel>()
                val context = LocalContext.current

                Navigation(
                    viewModel = viewModel,
                    context = context
                )
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