package com.sergimarrahyarenas.bloodstats.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "BloodStats")
                },
                navigationIcon = {
                    var expanded by rememberSaveable { mutableStateOf(false) }
                    IconButton(
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu"
                        )
                    }
                    DropdownMenuItem(
                        text = {
                               Text(text = "Perfil")
                        },
                        onClick = {

                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = "Profile"
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Opciones")
                        },
                        onClick = {

                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Options"
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Cerrar Sesión")
                        },
                        onClick = {

                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                                contentDescription = "Sign out"
                            )
                        }
                    )
                },
                actions = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile"
                        )
                    }
                },
                modifier = Modifier
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {

        }
    }
}