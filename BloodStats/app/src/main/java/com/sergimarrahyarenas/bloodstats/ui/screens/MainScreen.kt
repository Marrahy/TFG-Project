package com.sergimarrahyarenas.bloodstats.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sergimarrahyarenas.api.viewmodel.BlizzardViewModel

@Composable
fun MainScreen(blizzardViewModel: BlizzardViewModel) {
    val characterInfo by blizzardViewModel.characterData.observeAsState()

    var entityNameInput by remember {
        mutableStateOf("")
    }
    var realm by remember {
        mutableStateOf("")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = entityNameInput,
                onValueChange = {
                    entityNameInput = it
                }
            )
            Spacer(
                modifier = Modifier
                .padding(16.dp)
            )
            OutlinedTextField(
                value = realm,
                onValueChange = {
                    realm = it
                }
            )
        }
        Button(
            onClick = {
                blizzardViewModel.loadBossData()
                blizzardViewModel.loadCharacterData(entityNameInput, realm)
                Log.d("Nombre: ", entityNameInput)
            }
        ) {
            Text(text = "Get Stats")
        }

        Text(text = """Character stats: ${characterInfo?.name}""")
    }
}

fun openLinkInBrowser(context: Context, linkUrl: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
    context.startActivity(intent)
}