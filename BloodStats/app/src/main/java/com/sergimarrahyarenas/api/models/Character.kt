package com.sergimarrahyarenas.api.models

data class Character(
    val id: Int,
    val key: Key,
    val name: String,
    val realm: Realm
)