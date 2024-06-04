package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.realm

data class Realm(
    val _links: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.realm.Links,
    val realms: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.realm.RealmInfo>
)