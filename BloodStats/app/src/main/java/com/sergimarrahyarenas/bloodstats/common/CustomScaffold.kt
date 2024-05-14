package com.sergimarrahyarenas.bloodstats.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.GoogleAuthUiClient
import com.sergimarrahyarenas.bloodstats.models.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.navigation.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(
    content: @Composable () -> Unit,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    coroutineScope: CoroutineScope
    ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "BloodStats") },
                navigationIcon = {
                    var expanded by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Perfil") },
                            onClick = {
                                navController.navigate(route = Routes.ProfileScreen.route)
                            },
                            trailingIcon = {
                                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Perfil")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Buscar") },
                            onClick = {
                                navController.navigate(route = Routes.SearchScreen.route)
                            },
                            trailingIcon = {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Opciones") },
                            onClick = {
                                navController.navigate(route = Routes.OptionsScreen.route)
                            },
                            trailingIcon = {
                                Icon(imageVector = Icons.Default.Settings, contentDescription = "Opciones")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Cerrar sesión") },
                            onClick = {
                                coroutineScope.launch(Dispatchers.IO) {
                                    googleAuthUiClient.signOut()
                                }
                                navController.navigate(route = Routes.LoginScreen.route)
                            },
                            trailingIcon = {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                            }
                        )
                    }
                }
            )
        },
        content = {
            Surface(
                color = Color.White,
                modifier = Modifier.padding(it)
            ) {
                content()
            }
        }
    )
}

@Composable
fun GridListSwitch(
    listViewChecked: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Ver como: Cuadrícula")
        Switch(
            checked = listViewChecked,
            onCheckedChange = { onValueChange(it) },
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(text = "Lista")
    }
}

//@Composable
//fun EntitiesVerticalGrid(item: ItemMedia, items: List<ItemData>) {
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(3),
//        verticalArrangement = Arrangement.spacedBy(4.dp),
//        horizontalArrangement = Arrangement.spacedBy(4.dp)
//    ) {
//        items(items) { item ->
//
//        }
//    }
//}

//@Composable
//fun ItemInfoGreed(item: ItemMedia, itemInfo: ItemData) {
//    Card(
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
//        modifier = Modifier.padding(4.dp)
//    ) {
//        AsyncImage(
//            model = item.assets[0].value,
//            contentDescription = "Item Image",
//            modifier = Modifier
//                .clip(RoundedCornerShape(8.dp))
//                .fillMaxSize(),
//            contentScale = ContentScale.Crop
//        )
//        Text(
//            text = "${itemInfo.results}",
//            fontSize = 12.sp,
//            maxLines = 1,
//            softWrap = false,
//            overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
//        )
//    }
//}

@Composable
fun CharacterInfoGreed(character: CharacterMedia?) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.padding(4.dp)
    ) {
        AsyncImage(
            model = character?.assets?.get(1),
            contentDescription = "Character Image",
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "${character?.character?.name}",
            fontSize = 12.sp,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        )
    }
}