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
import com.sergimarrahyarenas.bloodstats.ui.navigation.Navigation
import com.sergimarrahyarenas.bloodstats.ui.theme.BloodStatsTheme
import com.sergimarrahyarenas.bloodstats.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent {
                val context = LocalContext.current
                val userViewModel = UserViewModel(context)

                Navigation(userViewModel = userViewModel, context = context)
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