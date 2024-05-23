package com.sergimarrahyarenas.bloodstats.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sergimarrahyarenas.bloodstats.R
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.ui.common.CustomScaffold
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import com.sergimarrahyarenas.bloodstats.ui.common.DynamicButton
import com.sergimarrahyarenas.bloodstats.viewmodel.BlizzardViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun CharacterGuildScreen(
    blizzardViewModel: BlizzardViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    navController: NavController,
    coroutineScope: CoroutineScope,
    context: Context
) {
    val characterGuild by blizzardViewModel.characterGuildRoster.observeAsState()
    val characterProfileSummary by blizzardViewModel.characterProfileSummary.observeAsState()
    val characterActiveSpecialization by blizzardViewModel.characterActiveSpecialization.observeAsState()
    val listOfMembersMedia by blizzardViewModel.membersMedia.observeAsState()

    CustomScaffold(
        navController = navController,
        googleAuthUiClient = googleAuthUiClient,
        coroutineScope = coroutineScope,
        context = context,
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                characterGuild?.guild?.let { Text(text = it.name) }
                Spacer(modifier = Modifier.padding(8.dp))

                if ("ALLIANCE" == characterGuild?.guild?.faction?.type) {
                    Image(
                        painter = painterResource(id = R.drawable.alliancelogo),
                        contentDescription = "Allianze Logo"
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.hordelogo),
                        contentDescription = "Horde Logo"
                    )
                }
                
                Text(text = "Lista de miembros")
                Spacer(modifier = Modifier.padding(8.dp))
                Spacer(modifier = Modifier.padding(8.dp))

                DynamicButton(
                    currentScreen = Routes.CharacterGuildScreen.route,
                    navController = navController,
                    blizzardViewModel = blizzardViewModel,
                    characterProfileSummary = characterProfileSummary,
                    characterActiveSpecialization = characterActiveSpecialization
                )

                Spacer(modifier = Modifier.padding(8.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    characterGuild?.members?.size?.let {
                        items(it) { member ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                ClickableText(
                                    AnnotatedString(
                                        text = characterGuild!!.members[member].character.name
                                    )
                                ) {
                                    blizzardViewModel.loadCharacterProfileSummaryEquipmentMedia(
                                        characterGuild!!.members[member].character.name,
                                        characterGuild!!.members[member].character.realm.slug
                                    )
                                    navController.navigate(route = Routes.CharacterEquipmentScreen.route)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}